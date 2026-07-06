package com.project.security.service.Impl;

import com.project.security.entity.RefreshToken;
import com.project.security.entity.UserInfo;
import com.project.security.request.AuthRequestDto;
import com.project.security.response.AuthResponseDto;
import com.project.security.service.AuthService;
import com.project.security.service.RefreshTokenService;
import com.project.security.utils.JwtUtil;
import com.tasnim.commonlibrary.exceptions.UnauthorizedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    RefreshTokenService refreshTokenService;

    @Override
    public AuthResponseDto getToken(AuthRequestDto authRequestDto) {
        log.info("AuthServiceImpl - Generating token for user: {}", authRequestDto.getUsername());

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequestDto.getUsername(), authRequestDto.getPassword())
        );

        log.info("User authenticated successfully: {}", authRequestDto.getUsername());

        UserInfo userDetails = (UserInfo) authentication.getPrincipal();
        if (userDetails == null) {
            log.error("UserDetails is null after authentication for user: {}", authRequestDto.getUsername());
            throw new UnauthorizedException("User details not found after authentication");
        }

        String accessToken = jwtUtil.generateToken(userDetails);
        RefreshToken refreshToken = refreshTokenService.generateToken(userDetails);

        return AuthResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getToken())
                .build();
    }

    @Override
    public AuthResponseDto authenticateByRefreshToken(String refreshToken) {
        return refreshTokenService.findByToken(refreshToken)
                .map(refreshTokenService::verifyToken)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String accessToken = jwtUtil.generateToken(user);
                    return AuthResponseDto.builder()
                            .accessToken(accessToken)
                            .refreshToken(refreshToken)
                            .build();
                })
                .orElseThrow(() -> new UnauthorizedException("Invalid refresh refreshToken. Please login again."));
    }
}
