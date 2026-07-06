package com.project.security.service.Impl;

import com.project.security.event.UserPublisher;
import com.project.security.request.UserRequestDto;
import com.project.security.response.UserResponseDto;
import com.project.security.entity.Roles;
import com.project.security.entity.UserInfo;
import com.project.security.repository.UserDetailsRepository;
import com.project.security.service.RoleRightService;
import com.project.security.service.UserService;
import com.tasnim.commonlibrary.exceptions.BadRequestException;
import com.tasnim.commonlibrary.exceptions.ForbiddenException;
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
        log.info("Request received to register user: {}", userRequestDto);

        validateUser(userRequestDto);
        Set<Roles> roles = roleRightService.getRolesByIds(userRequestDto.getRoles());

        if(roles.stream().anyMatch(r -> r.getName().equalsIgnoreCase("admin"))) {
            throw new ForbiddenException(
                    "Admin role cannot be assigned to an user");
        }

        return processRegistration(userRequestDto, roles);
    }

    @Override
    public UserResponseDto registerAdmin(UserRequestDto userRequestDto) {
        log.info("Request received to register admin: {}", userRequestDto);

        validateUser(userRequestDto);
        Set<Roles> roles = roleRightService.getRolesByIds(userRequestDto.getRoles());
        return processRegistration(userRequestDto, roles);
    }

    private void validateUser(UserRequestDto userRequestDto) {
        if(userDetailsRepository.existsByUsername(userRequestDto.getUsername())){
            throw new BadRequestException("Username already exists: " + userRequestDto.getUsername());
        }
    }

    private UserResponseDto processRegistration(UserRequestDto userRequestDto, Set<Roles> roles) {
        UserInfo user = UserInfo.builder()
                .id(UUID.randomUUID().toString())
                .username(userRequestDto.getUsername())
                .password(passwordEncoder.encode(userRequestDto.getPassword()))
                .role(roles)
                .build();
        UserInfo savedUser = userDetailsRepository.save(user);
        log.info("Registration complete: {}", savedUser.getUsername());

        //publish event to Kafka
        userRequestDto.setUserId(savedUser.getId());
        userRequestDto.setPassword(null);
        userPublisher.publishUserInfo(userRequestDto);

        return UserResponseDto.builder()
                .id(savedUser.getId())
                .username(savedUser.getUsername())
                .role(roles.stream().map(Roles::getName).toList())
                .build();
    }
}
