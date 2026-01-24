package com.wiensquare.user.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wiensquare.user.configuration.Registry;
import com.wiensquare.user.domain.entity.UserEntity;
import com.wiensquare.user.domain.repository.UserRepository;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth instanceof ApiKeyAuthentication apiKeyAuthentication) {
            if (auth.getAuthorities().stream().filter(a -> a.getAuthority().equals("APP")).findFirst().isPresent()) {
                String authHeader = request.getHeader("Authorization");

                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    try {
                        String token = authHeader.substring(7);
                        String username = jwtUtil.extractUsername(token);


                        Optional<UserEntity> userEntityOpt = userRepository.findByUsername(username);
                        if (userEntityOpt.isPresent()) {
                            List<GrantedAuthority> authorities = new ArrayList<>(apiKeyAuthentication.getAuthorities());
                            authorities.add(new SimpleGrantedAuthority("USER"));
                            UsernamePasswordAuthenticationToken pwAuth =
                                    new UsernamePasswordAuthenticationToken(username, null, authorities);
                            SecurityContextHolder.getContext().setAuthentication(pwAuth);
                            Registry.setCurrentUser(userEntityOpt.get());
                        } else {
                            throw new JwtException(String.format("No User %s found!", username));
                        }
                    } catch (JwtException e) {
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                        OutputStream responseOutputStream = response.getOutputStream();
                        Map<String, String> data = new HashMap<>();
                        data.put("errorCode", HttpStatus.UNAUTHORIZED.getReasonPhrase());
                        data.put("message", e.getMessage());
                        objectMapper.writeValue(responseOutputStream, data);
                        return;
                    }
                }
            }
        }

        filterChain.doFilter(request, response);
    }

}
