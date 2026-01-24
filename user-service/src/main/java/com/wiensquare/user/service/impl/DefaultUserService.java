package com.wiensquare.user.service.impl;

import com.wiensquare.user.configuration.Registry;
import com.wiensquare.user.domain.entity.UserEntity;
import com.wiensquare.user.domain.repository.UserRepository;
import com.wiensquare.user.exception.UserDoesNotExistException;
import com.wiensquare.user.exception.UserPasswordMismatchException;
import com.wiensquare.user.rest.openapi.server.model.LoginRequestModel;
import com.wiensquare.user.rest.openapi.server.model.LoginResponseModel;
import com.wiensquare.user.rest.openapi.server.model.RegisterRequestModel;
import com.wiensquare.user.rest.openapi.server.model.RegisterResponseModel;
import com.wiensquare.user.security.JwtUtil;
import com.wiensquare.user.service.RefreshTokenService;
import com.wiensquare.user.service.UserService;
import jakarta.persistence.EntityExistsException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.ZoneOffset;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class DefaultUserService implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtUtil jwtUtil;

    private final RefreshTokenService refreshTokenService;

    @Transactional
    @Override
    public RegisterResponseModel registerUser(RegisterRequestModel registerRequest) {
        log.info("Register Request received: {}", registerRequest);

        boolean userExists = userRepository.existsByUsername(registerRequest.getUsername());

        final UserEntity userEntity;

        if (userExists) {
            log.error("User with name {} already exists", registerRequest.getUsername());
            throw new EntityExistsException("Username already exists");
        } else {
            UserEntity user = new UserEntity();
            user.setUsername(registerRequest.getUsername());
            user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
            userEntity = userRepository.save(user);
        }

        return mapUserToRegisterResponseModel(userEntity);
    }

    private RegisterResponseModel mapUserToRegisterResponseModel(UserEntity userEntity) {
        RegisterResponseModel registerResponseModel = new RegisterResponseModel();
        registerResponseModel.setUsername(userEntity.getUsername());
        registerResponseModel.setId(userEntity.getId());
        registerResponseModel.setCreatedAt(userEntity.getCreationTimestamp().atOffset(ZoneOffset.UTC));
        return registerResponseModel;
    }

    @Transactional
    @Override
    public LoginResponseModel loginUser(LoginRequestModel loginRequest) {
        Optional<UserEntity> userEntityOpt = userRepository.findByUsername(loginRequest.getUsername());

        if (userEntityOpt.isPresent()) {
            UserEntity user = userEntityOpt.get();
            if (passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                LoginResponseModel loginResponseModel = new LoginResponseModel();
                String token = jwtUtil.generateToken(user.getUsername(), user.getId());
                String refreshToken = refreshTokenService.createRefreshToken(user);
                loginResponseModel.setToken(token);
                loginResponseModel.setRefreshToken(refreshToken);
                return loginResponseModel;
            } else {
                throw new UserPasswordMismatchException("Wrong password provided");
            }
        } else {
            throw new UserDoesNotExistException("Username not found");
        }
    }

    @Override
    public UserEntity getCurrentUser() {
        return Registry.getCurrentUser();
    }
}
