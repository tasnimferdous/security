package com.project.security.service;

import com.project.security.dto.AuthRequestDto;

public interface AuthService {
    String getToken(AuthRequestDto authRequestDto);
}
