package com.wiensquare.user.controller;

import com.wiensquare.user.rest.openapi.server.api.RefreshApi;
import com.wiensquare.user.rest.openapi.server.model.LoginResponseModel;
import com.wiensquare.user.rest.openapi.server.model.RefreshRequestModel;
import com.wiensquare.user.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RefreshController implements RefreshApi {

    private final RefreshTokenService refreshTokenService;

    @Override
    public ResponseEntity<LoginResponseModel> refresh(RefreshRequestModel refreshRequestModel) {
        return ResponseEntity.ok(refreshTokenService.refresh(refreshRequestModel));
    }

}
