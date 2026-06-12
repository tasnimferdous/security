package com.project.security.service.Impl;

import com.project.security.event.UserPublisher;
import com.project.security.request.UserRequestDto;
import com.project.security.response.UserResponseDto;
import com.project.security.entity.Roles;
import com.project.security.entity.UserInfo;
import com.project.security.repository.UserDetailsRepository;
import com.project.security.service.RoleRightService;
import com.project.security.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserDetailsRepository userDetailsRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRightService roleRightService;
    private final UserPublisher userPublisher;

    public UserServiceImpl(UserDetailsRepository userDetailsRepository, PasswordEncoder passwordEncoder, RoleRightService roleRightService, UserPublisher userPublisher) {
        this.userDetailsRepository = userDetailsRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRightService = roleRightService;
        this.userPublisher = userPublisher;
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

            UserInfo user = UserInfo.builder()
                    .id(UUID.randomUUID().toString())
                    .username(userRequestDto.getUsername())
                    .password(passwordEncoder.encode(userRequestDto.getPassword()))
                    .role(roles)
                    .build();
            UserInfo savedUser = userDetailsRepository.save(user);
            log.info("User registered successfully: {}", savedUser.getUsername());

            //publish event to Kafka
            userRequestDto.setUserId(savedUser.getId());
            userPublisher.publishUserInfo(userRequestDto);

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
