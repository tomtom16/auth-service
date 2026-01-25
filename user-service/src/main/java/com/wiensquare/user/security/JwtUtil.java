package com.wiensquare.user.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

@Component
public class JwtUtil {

    private final String SECRET = "MVvYNYnB+mUSmEKRd58gG5Bd&2vHQEL2";
    private final long EXPIRATION = 1000 * 60 * 60; // 1 hour


    public String generateToken(String username, UUID userId) {
        return Jwts.builder()
                .setSubject(username)
                .claim("scope", "read write")
                .claim("uid", userId.toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(Keys.hmacShaKeyFor(SECRET.getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }


    public String extractUsername(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

}
