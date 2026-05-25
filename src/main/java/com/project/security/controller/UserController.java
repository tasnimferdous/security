package com.project.security.controller;

import com.project.security.request.UserRequestDto;
import com.project.security.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/user/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRequestDto userRequestDto){
        log.info("Request received to register user: {}", userRequestDto);
        userRequestDto.setRoles(List.of(2));
        return ResponseEntity.ok(userService.registerUser(userRequestDto));
    }

    @PostMapping("/admin/register")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> registerAdmin(@RequestBody UserRequestDto userRequestDto){
        return ResponseEntity.ok(userService.registerUser(userRequestDto));
    }
}
