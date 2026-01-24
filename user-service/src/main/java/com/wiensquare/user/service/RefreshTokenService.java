package com.wiensquare.user.service;

import com.wiensquare.user.domain.entity.UserEntity;
import com.wiensquare.user.rest.openapi.server.model.LoginResponseModel;
import com.wiensquare.user.rest.openapi.server.model.RefreshRequestModel;

public interface RefreshTokenService {

    String createRefreshToken(UserEntity user);

    UserEntity validate(String rawToken);

    LoginResponseModel refresh(RefreshRequestModel refreshRequest);

}
