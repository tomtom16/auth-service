package com.wiensquare.user.security;

import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Service
public class AuthenticationService {

    private static final String ADMIN_USER = "ADMIN";
    private static final String SUPPORT_USER = "SUPPORT";
    private static final String DEFAULT_IOS_APP = "IOS";
    private static final String DEFAULT_ANDROID_APP = "ANDROID";
    private static final String DEFAULT_WEB_APP = "WEB";

    private static final String[] APPS = {DEFAULT_IOS_APP, DEFAULT_ANDROID_APP, DEFAULT_WEB_APP};

    private static final String AUTH_TOKEN_HEADER_NAME = "X-Api-Key";

    private static final BadCredentialsException EXCEPTION = new BadCredentialsException("Invalid API Key");

    public Authentication getAuthentication(HttpServletRequest request) {
        String xApiKey = request.getHeader(AUTH_TOKEN_HEADER_NAME);
        if (xApiKey == null) {
            throw EXCEPTION;
        }

        final String userName;
        if (xApiKey.equals("11111")) {
        	userName = ADMIN_USER;
        } else if (xApiKey.equals("11112")) {
            userName = SUPPORT_USER;
        } else if (xApiKey.equals("11113")) {
            userName = DEFAULT_WEB_APP;
        } else if (xApiKey.equals("11114")) {
            userName = DEFAULT_IOS_APP;
        } else if (xApiKey.equals("11115")) {
            userName = DEFAULT_ANDROID_APP;
        } else {
        	throw EXCEPTION;
        }
        
        List<GrantedAuthority> authorities;
        if (ADMIN_USER.equals(userName)) {
        	authorities = AuthorityUtils.createAuthorityList("ADMIN");
        } else if (SUPPORT_USER.equals(userName)) {
            authorities = AuthorityUtils.createAuthorityList("SUPPORT");
        } else if (ArrayUtils.contains(APPS, userName)) {
            authorities = AuthorityUtils.createAuthorityList("APP");
        } else {
            authorities = AuthorityUtils.NO_AUTHORITIES;
        }

        return new ApiKeyAuthentication(xApiKey, userName, authorities);
    }
}