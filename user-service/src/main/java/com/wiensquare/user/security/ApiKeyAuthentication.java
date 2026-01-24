package com.wiensquare.user.security;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode(callSuper = true)
public class ApiKeyAuthentication extends AbstractAuthenticationToken {

    private static final long serialVersionUID = 4260683112109417585L;
    
	@Getter
    private final String apiKey;
    private final String name;

    public ApiKeyAuthentication(String apiKey, String name, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.apiKey = apiKey;
        this.name = name;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return name;
    }
}