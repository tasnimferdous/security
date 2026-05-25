package com.project.security.service.Impl;

import com.project.security.entity.RefreshToken;
import com.project.security.entity.UserInfo;
import com.project.security.repository.TokenRepository;
import com.project.security.service.CustomUserDetailsService;
import com.project.security.service.RefreshTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private final CustomUserDetailsService customUserDetailsService;
    private final TokenRepository tokenRepository;

    public RefreshTokenServiceImpl(CustomUserDetailsService customUserDetailsService, TokenRepository tokenRepository) {
        this.customUserDetailsService = customUserDetailsService;
        this.tokenRepository = tokenRepository;
    }


    //Here duplicate entry exception occurs.
    //I've to fix the issue
    @Override
    public RefreshToken generateToken(String username){
        log.info("RefreshTokenService - generateToken - username: {}", username);

        UserInfo userInfo = customUserDetailsService.findUserByUsername(username);

        RefreshToken refreshToken = RefreshToken.builder()
                .user(userInfo)
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusSeconds( 24 * 60 * 60))
                .build();

        tokenRepository.save(refreshToken);
        return refreshToken;
    }

    @Override
    public RefreshToken verifyToken(RefreshToken token){
        log.info("RefreshTokenService - verifyToken - token: {}", token);

        if (token.getExpiryDate().isBefore(Instant.now())) {
            log.warn("Refresh token expired: {}", token.getToken());
            throw new RuntimeException("Refresh token expired. Please login again.");
        }
        return token;
    }

    @Override
    public Optional<RefreshToken> findByToken(String token){
        log.info("RefreshTokenService - findByToken - token: {}", token);
        return tokenRepository.findByToken(token);
    }
}
