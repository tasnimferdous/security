package com.project.security.controller;

import com.project.security.request.UserRequestDto;
import com.project.security.response.UserResponseDto;
import com.project.security.service.UserService;
import com.tasnim.commonlibrary.model.CommonResponse;
import com.tasnim.commonlibrary.utils.ResponseUtil;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/register")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/user")
    public CommonResponse<UserResponseDto> registerUser(@RequestBody @Valid UserRequestDto userRequestDto){
        UserResponseDto response = userService.registerUser(userRequestDto);
        return ResponseUtil.success(response, "Registration completed successfully");
    }

    @PostMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public CommonResponse<UserResponseDto> registerAdmin(@RequestBody @Valid UserRequestDto userRequestDto){
        UserResponseDto response = userService.registerAdmin(userRequestDto);
        return ResponseUtil.success(response, "Registration completed successfully");
    }
}
