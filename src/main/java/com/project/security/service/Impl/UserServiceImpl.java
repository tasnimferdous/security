package com.project.security.service.Impl;

import com.project.security.dto.UserRequestDto;
import com.project.security.dto.UserResponseDto;
import com.project.security.entity.Users;
import com.project.security.repository.UserDetailsRepository;
import com.project.security.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserDetailsRepository userDetailsRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserDetailsRepository userDetailsRepository, PasswordEncoder passwordEncoder) {
        this.userDetailsRepository = userDetailsRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserResponseDto registerUser(UserRequestDto userRequestDto) {
        if(userDetailsRepository.findByUsername(userRequestDto.getUsername()).isPresent()){
            throw new RuntimeException("Username already exists: " + userRequestDto.getUsername());
        }

        Users user = Users.builder()
                .username(userRequestDto.getUsername())
                .password(passwordEncoder.encode(userRequestDto.getPassword()))
                .role(userRequestDto.getRole())
                .build();
        Users savedUser = userDetailsRepository.save(user);
        log.info("User registered successfully: {}", savedUser.getUsername());

        return UserResponseDto.builder()
                .id(savedUser.getId())
                .username(savedUser.getUsername())
                .role(savedUser.getRole())
                .build();
    }
}
