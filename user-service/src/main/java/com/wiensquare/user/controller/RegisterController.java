package com.wiensquare.user.controller;

import com.wiensquare.user.rest.openapi.server.api.RegisterApi;
import com.wiensquare.user.rest.openapi.server.model.RegisterRequestModel;
import com.wiensquare.user.rest.openapi.server.model.RegisterResponseModel;
import com.wiensquare.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RegisterController implements RegisterApi {

    private final UserService userService;

    @Override
    public ResponseEntity<RegisterResponseModel> registerUser(RegisterRequestModel registerRequest) {
        try {
            return ResponseEntity.ok(userService.registerUser(registerRequest));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

}
