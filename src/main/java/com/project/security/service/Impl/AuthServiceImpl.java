package com.project.security.service.Impl;

import com.project.security.dto.AuthRequestDto;
import com.project.security.service.AuthService;
import com.project.security.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    JwtUtil jwtUtil;
    @Override
    public String getToken(AuthRequestDto authRequestDto) {
        log.info("Generating token for user: {}", authRequestDto.getUsername());
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequestDto.getUsername(), authRequestDto.getPassword())
            );
            return jwtUtil.generateToken(authRequestDto.getUsername());
        }catch (Exception e){
            log.error("Exception - ",e);
            throw new RuntimeException(e.getMessage());
        }
    }
}
