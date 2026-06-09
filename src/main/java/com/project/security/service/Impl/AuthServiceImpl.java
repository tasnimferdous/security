package com.project.security.service.Impl;

import com.project.security.entity.RefreshToken;
import com.project.security.entity.UserInfo;
import com.project.security.request.AuthRequestDto;
import com.project.security.response.AuthResponseDto;
import com.project.security.service.AuthService;
import com.project.security.service.RefreshTokenService;
import com.project.security.utils.JwtUtil;
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

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequestDto.getUsername(), authRequestDto.getPassword())
            );

            if (authentication.isAuthenticated()) {
                log.info("User authenticated successfully: {}", authRequestDto.getUsername());
            } else {
                log.warn("Authentication failed for user: {}", authRequestDto.getUsername());
                throw new RuntimeException("Invalid username or password");
            }

            UserInfo userDetails = (UserInfo) authentication.getPrincipal();

            assert userDetails != null;
            String accessToken = jwtUtil.generateToken(userDetails);
            RefreshToken refreshToken = refreshTokenService.generateToken(userDetails);

            //publish event to Kafka

            return AuthResponseDto.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken.getToken())
                    .build();

        }catch (Exception e){
            log.error("Exception - ",e);
            throw new RuntimeException(e.getMessage());
        }
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
                .orElseThrow(() -> new RuntimeException("Invalid refresh refreshToken. Please login again."));
    }
}
