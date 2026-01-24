package com.wiensquare.user.controller;

import com.wiensquare.user.rest.openapi.server.api.LoginApi;
import com.wiensquare.user.rest.openapi.server.model.LoginRequestModel;
import com.wiensquare.user.rest.openapi.server.model.LoginResponseModel;
import com.wiensquare.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LoginController implements LoginApi {

    private final UserService userService;

    @Override
    public ResponseEntity<LoginResponseModel> login(LoginRequestModel loginRequest) {
        try {
            return ResponseEntity.ok(userService.loginUser(loginRequest));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

}
