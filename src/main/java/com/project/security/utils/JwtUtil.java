package com.project.security.utils;

import com.project.security.entity.UserInfo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtUtil {
    private final long EXPIRATION_TIME = 1000 * 60 * 60; // 1 hour
    private final String secretKey = "ThisIsASecretKeyForJwtTokenGeneration";
    private final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(secretKey.getBytes());

    public String generateToken(UserInfo userInfo){
        List<String> authorities =
                userInfo.getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .toList();

        return Jwts.builder()
                .subject(userInfo.getUsername())
                .claim("userId", userInfo.getId())
                .claim("authorities", authorities)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SECRET_KEY)
                .compact();
    }

    public String extractUserId(String token) {
        return extractClaims(token).get("userId", String.class);
    }

    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    public List<String> extractAuthorities(String token) {
        List<?> authorities = extractClaims(token).get("authorities", List.class);
        return authorities != null
            ? authorities.stream()
                .map(Object::toString)
                .collect(Collectors.toList())
            : Collections.emptyList();
    }


    private Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(SECRET_KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean validateToken(String token) {
        return !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }
}
