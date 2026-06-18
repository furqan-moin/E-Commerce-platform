package com.furqan.ecommerce.service;


import com.furqan.ecommerce.dto.UserRequestDto;
import com.furqan.ecommerce.dto.UserResponseDto;
import com.furqan.ecommerce.entity.UserEntity;

import com.furqan.ecommerce.exception.UserAlreadyExistsException;
import com.furqan.ecommerce.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final IUserRepository userRepository;

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
                        new RuntimeException("user not found with id: " + id));
        userEntity.setIsActive(requestedIsActive);
        userRepository.save(userEntity);
    }

    public UserResponseDto updateUserDetails(Long id, UserRequestDto userRequestDto) {
        if (id == null) {
            throw new IllegalArgumentException("user_id is required");
        }
        UserEntity user = userRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException("user not found with id: " + id));

        if (userRequestDto.getEmail() != null && !userRequestDto.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(userRequestDto.getEmail())) {
                throw new UserAlreadyExistsException("user already exists with email : " + userRequestDto.getEmail());
            }
            throw new UserAlreadyExistsException(
                    "user already exists with email : " + userRequestDto.getEmail());
        }
        user.setEmail(userRequestDto.getEmail());

        if (userRequestDto.getFirstName() != null) {
            user.setFirstName(userRequestDto.getFirstName());
        }
        if (userRequestDto.getMiddleName() != null) {
            user.setMiddleName(userRequestDto.getMiddleName());
        }
        if (userRequestDto.getLastName() != null) {
            user.setLastName(userRequestDto.getLastName());
        }
        if (userRequestDto.getPassword() != null && !userRequestDto.getPassword().isBlank()) {
            user.setPassword(userRequestDto.getPassword()); // hash when you add security
        }
        if (userRequestDto.getPhoneNumber() != null) {
            user.setPhoneNumber(userRequestDto.getPhoneNumber());
        }
        if (userRequestDto.getCountryCode() != null) {
            user.setCountryCode(userRequestDto.getCountryCode());
        }
        if (userRequestDto.getLanguage() != null) {
            user.setLanguage(userRequestDto.getLanguage());
        }
        return toResponseDto(userRepository.save(user));
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
