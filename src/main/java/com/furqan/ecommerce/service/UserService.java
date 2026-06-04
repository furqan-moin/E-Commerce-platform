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
        UserEntity userEntity = new UserEntity();
        userEntity.setName(userRequestDto.getName());
        userEntity.setEmail(userRequestDto.getEmail());
        userEntity.setPassword(userRequestDto.getPassword());
        userEntity.setPhoneNumber(userRequestDto.getPhoneNumber());
        userEntity.setLanguage(userRequestDto.getLanguage());
        userEntity.setIsActive(true);

        UserEntity savedUser = userRepository.save(userEntity);
        return UserResponseDto.builder()
                .userId(savedUser.getUserId())
                .name(savedUser.getName())
                .email(savedUser.getEmail())
                .phoneNumber(savedUser.getPhoneNumber())
                .isActive(savedUser.getIsActive())
                .language(savedUser.getLanguage())
                .build();
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
}