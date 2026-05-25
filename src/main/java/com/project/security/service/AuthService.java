package com.project.security.service;

import com.project.security.request.AuthRequestDto;
import com.project.security.response.AuthResponseDto;

public interface AuthService {
    AuthResponseDto getToken(AuthRequestDto authRequestDto);

    AuthResponseDto authenticateByRefreshToken(String refreshToken);
}
