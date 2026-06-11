package com.furqan.ecommerce.service;


import com.furqan.ecommerce.dto.UserRequestDto;
import com.furqan.ecommerce.dto.UserResponseDto;
import com.furqan.ecommerce.entity.UserEntity;

import com.furqan.ecommerce.exception.UserAlreadyExistsException;
import com.furqan.ecommerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    public UserResponseDto createUser(UserRequestDto userRequestDto) {
        if (userRepository.existsByEmail(userRequestDto.getEmail())) {
            throw new UserAlreadyExistsException(
                    "user already exists with email : " + userRequestDto.getEmail()
            );
        }
        UserEntity userEntity = UserEntity.builder()
                .firstName(userRequestDto.getFirstName())
                .middleName(userRequestDto.getMiddleName())
                .lastName(userRequestDto.getLastName())
                .email(userRequestDto.getEmail())
                .password(userRequestDto.getPassword())
                .phoneNumber(userRequestDto.getPhoneNumber())
                .countryCode(userRequestDto.getCountryCode())
                .language(userRequestDto.getLanguage() != null ? userRequestDto.getLanguage() : "english")
                .isActive(true)
                .build();

        UserEntity savedUser = userRepository.save(userEntity);

        return toResponseDto(savedUser);
    }

    public UserEntity getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new RuntimeException("User not found!"));
    }

    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    public void updateUserStatus(Long id, boolean requestedIsActive) {
        UserEntity userEntity = userRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException("user not found with id: "+id));
        userEntity.setIsActive(requestedIsActive);
        userRepository.save(userEntity);
    }

    private UserResponseDto toResponseDto(UserEntity user) {
        return UserResponseDto.builder()
                .userId(user.getUserId())
                .firstName(user.getFirstName())
                .middleName(user.getMiddleName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .countryCode(user.getCountryCode())
                .isActive(user.getIsActive())
                .language(user.getLanguage())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
