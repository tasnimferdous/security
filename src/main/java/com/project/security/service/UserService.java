package com.project.security.service;

import com.project.security.dto.UserRequestDto;
import com.project.security.dto.UserResponseDto;

public interface UserService {
    UserResponseDto registerUser(UserRequestDto userRequestDto);
}
