package com.furqan.ecommerce.impl;

import com.furqan.ecommerce.dto.user.UserRequestDto;
import com.furqan.ecommerce.dto.user.UserResponseDto;

import java.util.List;

public interface IUserService {
    UserResponseDto createUser(UserRequestDto request);

    UserResponseDto getUser(long userId);

    List<UserResponseDto> getUsers(UserRequestDto request);

    UserResponseDto updateUser(Long userId,
                               UserRequestDto request);

    void deleteUser(Long userId);

    void deactivateUser(Long userId);
}
