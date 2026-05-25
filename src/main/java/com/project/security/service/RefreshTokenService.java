package com.project.security.service;

import com.project.security.entity.RefreshToken;

import java.util.Optional;

public interface RefreshTokenService {
    RefreshToken generateToken(String username);

    RefreshToken verifyToken(RefreshToken token);

    Optional<RefreshToken> findByToken(String token);
}
