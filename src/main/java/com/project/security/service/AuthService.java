package com.project.security.service;

import com.project.security.dto.AuthRequestDto;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {
    String generateToken(AuthRequestDto authRequestDto);
}
