package com.project.security.service;

import com.project.security.entity.RefreshToken;
import com.project.security.entity.UserInfo;

import java.util.Optional;

public interface RefreshTokenService {
    RefreshToken generateToken(UserInfo user);

    RefreshToken verifyToken(RefreshToken token);

    Optional<RefreshToken> findByToken(String token);
}
