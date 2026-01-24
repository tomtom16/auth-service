package com.wiensquare.user.security;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AuthenticationFilter extends GenericFilterBean {

    private static final String[] PERMITTED_URLS = {"/actuator/health", "/actuator/env", "/actuator/prometheus"};

    private static final String[] PROTECTED_URLS = {"/api", "/actuator"};

    private final AuthenticationService authenticationService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        if (request instanceof HttpServletRequest httpServletRequest && requestMatches(httpServletRequest.getRequestURI(), PROTECTED_URLS)) {
            try {
                Authentication authentication = authenticationService.getAuthentication(httpServletRequest);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (Exception e) {

                if (!requestMatches(httpServletRequest.getRequestURI(), PERMITTED_URLS)) {
                    HttpServletResponse httpResponse = (HttpServletResponse) response;
                    httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    httpResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    OutputStream responseOutputStream = httpResponse.getOutputStream();
                    Map<String, String> data = new HashMap<>();
                    data.put("errorCode", HttpStatus.UNAUTHORIZED.getReasonPhrase());
                    data.put("message", e.getMessage());
                    objectMapper.writeValue(responseOutputStream, data);
                }
            }
        }

        filterChain.doFilter(request, response);
    }

    private boolean requestMatches(String requestUri, String[] uris) {
        return Arrays.stream(uris).anyMatch(uri -> StringUtils.startsWithIgnoreCase(requestUri, uri));
    }
}