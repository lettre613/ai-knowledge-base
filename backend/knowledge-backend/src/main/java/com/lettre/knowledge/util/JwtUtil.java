package com.lettre.knowledge.util;


import com.lettre.knowledge.config.JwtProperties;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;



@Component
public class JwtUtil {


    private final JwtProperties jwtProperties;


    public JwtUtil(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }


    public String generateToken(Long userId, String username) {

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtProperties.getExpiration());

        return Jwts.builder()
                .subject(username)
                .claim("userId", userId)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSigningKey())
                .compact();

    }


    public String getUsernameFromToken(String token) {

        return parseClaims(token).getSubject();

    }


    public Long getUserIdFromToken(String token) {

        return parseClaims(token).get("userId", Long.class);

    }


    public boolean validateToken(String token) {

        try {
            parseClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }

    }


    private Claims parseClaims(String token) {

        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

    }


    private SecretKey getSigningKey() {

        return Keys.hmacShaKeyFor(
                jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8)
        );

    }


}
