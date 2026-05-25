package com.project.security.controller;

import com.project.security.request.AuthRequestDto;
import com.project.security.request.RefreshTokenRequestDto;
import com.project.security.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    @Autowired
    private AuthService authService;
    @PostMapping("/authenticate")
    public ResponseEntity<?> generateToken(@RequestBody AuthRequestDto authRequestDto){
        return ResponseEntity.ok(authService.getToken(authRequestDto));
    }

    @PostMapping("/authenticate/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequestDto requestDto){
        return ResponseEntity.ok(authService.authenticateByRefreshToken(requestDto.getRefreshToken()));
    }
}
