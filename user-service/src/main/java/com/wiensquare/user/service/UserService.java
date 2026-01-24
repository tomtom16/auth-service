package com.wiensquare.user.service;

import com.wiensquare.user.domain.entity.UserEntity;
import com.wiensquare.user.rest.openapi.server.model.LoginRequestModel;
import com.wiensquare.user.rest.openapi.server.model.LoginResponseModel;
import com.wiensquare.user.rest.openapi.server.model.RegisterRequestModel;
import com.wiensquare.user.rest.openapi.server.model.RegisterResponseModel;

public interface UserService {

    RegisterResponseModel registerUser(RegisterRequestModel registerRequest);

    LoginResponseModel loginUser(LoginRequestModel loginRequest);

    UserEntity getCurrentUser();

}
