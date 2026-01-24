package com.wiensquare.api.controller;

import com.wiensquare.api.rest.openapi.server.api.PingApi;
import com.wiensquare.api.rest.openapi.server.model.PingResponseModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;
import java.util.UUID;

@RestController
public class PingController implements PingApi {

    @PreAuthorize("isAuthenticated()")
    @Override
    public ResponseEntity<PingResponseModel> ping() {
        return ResponseEntity.ok(getPingResponseModel());
    }

    private PingResponseModel getPingResponseModel() {
        PingResponseModel response = new PingResponseModel();

        response.setText("pong");
        response.setTimestamp(OffsetDateTime.now());

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof Jwt jwt) {
            String username = jwt.getSubject();
            response.setUsername(username);
            response.setUid(UUID.fromString(jwt.getClaimAsString("uid")));
        }

        return response;
    }

}
