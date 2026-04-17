package com.project.security.service.Impl;

import com.project.security.dto.AuthRequestDto;
import com.project.security.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Slf4j
public class AuthServiceImpl implements AuthService {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Override
    public String generateToken(AuthRequestDto authRequestDto) {
        log.info("Generating token for user: {}", authRequestDto.getUsername());
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequestDto.getUsername(), authRequestDto.getPassword())
            );
        }catch (Exception e){
            throw new RuntimeException(e);
        }
        return "Token";
    }
}
