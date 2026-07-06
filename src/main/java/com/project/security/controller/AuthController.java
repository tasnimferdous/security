package com.project.security.controller;

import com.project.security.request.AuthRequestDto;
import com.project.security.request.RefreshTokenRequestDto;
import com.project.security.response.AuthResponseDto;
import com.project.security.service.AuthService;
import com.tasnim.commonlibrary.model.CommonResponse;
import com.tasnim.commonlibrary.utils.ResponseUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    @Autowired
    private AuthService authService;
    @PostMapping("/login")
    public CommonResponse<AuthResponseDto> generateToken(@RequestBody @Valid AuthRequestDto authRequestDto){
        AuthResponseDto response = authService.getToken(authRequestDto);
        return ResponseUtil.success(response, "Authentication successful");
    }

    @PostMapping("/refresh-token")
    public CommonResponse<AuthResponseDto> refreshToken(@RequestBody @Valid RefreshTokenRequestDto requestDto){
        AuthResponseDto response = authService.authenticateByRefreshToken(requestDto.getRefreshToken());
        return ResponseUtil.success(response, "Authentication successful");
    }
}
