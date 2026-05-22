package com.project.security.service.Impl;

import com.project.security.dto.UserRequestDto;
import com.project.security.dto.UserResponseDto;
import com.project.security.entity.Roles;
import com.project.security.entity.Users;
import com.project.security.repository.UserDetailsRepository;
import com.project.security.service.RoleRightService;
import com.project.security.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserDetailsRepository userDetailsRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRightService roleRightService;

    public UserServiceImpl(UserDetailsRepository userDetailsRepository, PasswordEncoder passwordEncoder, RoleRightService roleRightService) {
        this.userDetailsRepository = userDetailsRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRightService = roleRightService;
    }

    @Override
    public UserResponseDto registerUser(UserRequestDto userRequestDto) {
        if(userRequestDto.getRoles() == null || userRequestDto.getRoles().isEmpty()) {
            throw new RuntimeException("Role is required for user registration");
        }
        if(userDetailsRepository.findByUsername(userRequestDto.getUsername()).isPresent()){
            throw new RuntimeException("Username already exists: " + userRequestDto.getUsername());
        }

        try {
            Set<Roles> roles = roleRightService.getRolesByIds(userRequestDto.getRoles());

            Users user = Users.builder()
                    .username(userRequestDto.getUsername())
                    .password(passwordEncoder.encode(userRequestDto.getPassword()))
                    .role(roles)
                    .build();
            Users savedUser = userDetailsRepository.save(user);
            log.info("User registered successfully: {}", savedUser.getUsername());

            return UserResponseDto.builder()
                    .id(savedUser.getId())
                    .username(savedUser.getUsername())
                    .role(roles.stream().map(Roles::getName).toList())
                    .build();
        }catch (Exception e){
            log.error("Failed to register user: ", e);
            throw new RuntimeException(e.getMessage());
        }
    }
}
