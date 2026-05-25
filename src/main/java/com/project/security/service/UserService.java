package com.project.security.service;

import com.project.security.request.UserRequestDto;
import com.project.security.response.UserResponseDto;

public interface UserService {
    UserResponseDto registerUser(UserRequestDto userRequestDto);
}
