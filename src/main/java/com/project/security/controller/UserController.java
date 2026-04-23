package com.project.security.controller;

import com.project.security.dto.UserRequestDto;
import com.project.security.enums.Role;
import com.project.security.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/user/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRequestDto userRequestDto){
        userRequestDto.setRole(Role.USER);
        return ResponseEntity.ok(userService.registerUser(userRequestDto));
    }

    @PostMapping("/admin/register")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> registerAdmin(@RequestBody UserRequestDto userRequestDto){
        return ResponseEntity.ok(userService.registerUser(userRequestDto));
    }
}
