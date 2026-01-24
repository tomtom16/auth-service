package com.wiensquare.user.controller;

import com.wiensquare.user.domain.entity.UserEntity;
import com.wiensquare.user.rest.openapi.server.api.PingApi;
import com.wiensquare.user.rest.openapi.server.model.PingResponseModel;
import com.wiensquare.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;

@RestController
@RequiredArgsConstructor
public class PingController implements PingApi {

    private final UserService userService;

    @Override
    public ResponseEntity<PingResponseModel> ping() {
        return ResponseEntity.ok(getPingResponseModel());
    }

    private PingResponseModel getPingResponseModel() {
        PingResponseModel response = new PingResponseModel();

        response.setText("pong");
        response.setTimestamp(OffsetDateTime.now());

        UserEntity currentUser = userService.getCurrentUser();
        if (currentUser != null){
            response.setUsername(currentUser.getUsername());
        }

        return response;
    }

}
