package com.project.security.controller;

import com.project.security.dto.AuthRequestDto;
import com.project.security.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    @Autowired
    private AuthService authService;
    @PostMapping("/authenticate")
    public String generateToken(@RequestBody AuthRequestDto authRequestDto){
        return authService.generateToken(authRequestDto);
    }
}
