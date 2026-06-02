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

    @Override
    public RefreshToken generateToken(UserInfo user){
        log.info("RefreshTokenService - generateToken - username: {}", user.getUsername());

        RefreshToken refreshToken = tokenRepository
                .findByUser(user)
                .orElseGet(() -> RefreshToken.builder()
                        .user(user)
                        .build());

        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiryDate(
                Instant.now().plusSeconds(24 * 60 * 60)
        );

        return tokenRepository.save(refreshToken);
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
