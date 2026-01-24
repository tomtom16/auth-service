package com.wiensquare.user.service.impl;

import com.wiensquare.user.domain.entity.RefreshToken;
import com.wiensquare.user.domain.entity.UserEntity;
import com.wiensquare.user.domain.repository.RefreshTokenRepository;
import com.wiensquare.user.rest.openapi.server.model.LoginResponseModel;
import com.wiensquare.user.rest.openapi.server.model.RefreshRequestModel;
import com.wiensquare.user.security.JwtUtil;
import com.wiensquare.user.service.RefreshTokenService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class DefaultRefreshTokenService implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder encoder;
    private final JwtUtil jwtUtil;

    @Transactional
    @Override
    public String createRefreshToken(UserEntity user) {
        refreshTokenRepository.deleteByUser(user);

        String rawToken = UUID.randomUUID().toString();

        RefreshToken token = new RefreshToken();
        token.setUser(user);
        token.setTokenHash(encoder.encode(rawToken));
        token.setExpiryDate(LocalDateTime.now().plusDays(30));

        refreshTokenRepository.save(token);
        return rawToken;
    }

    @Override
    public UserEntity validate(String rawToken) {
        return refreshTokenRepository.findAll().stream()
                .filter(t -> encoder.matches(rawToken, t.getTokenHash()))
                .filter(t -> t.getExpiryDate().isAfter(LocalDateTime.now()))
                .map(RefreshToken::getUser)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));
    }

    @Transactional
    @Override
    public LoginResponseModel refresh(RefreshRequestModel refreshRequest) {

        UserEntity user = validate(refreshRequest.getRefreshToken());

        String newAccessToken = jwtUtil.generateToken(user.getUsername(), user.getId());

        LoginResponseModel response = new LoginResponseModel();

        response.setToken(newAccessToken);
        response.setRefreshToken(createRefreshToken(user));
        return response;
    }
}
