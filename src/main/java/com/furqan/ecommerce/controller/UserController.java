package com.furqan.ecommerce.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.furqan.ecommerce.dto.common.ApiResponse;
import com.furqan.ecommerce.dto.user.UserRequestDto;
import com.furqan.ecommerce.dto.user.UserResponseDto;
import com.furqan.ecommerce.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ecommerce/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final ObjectMapper objectMapper;


    @GetMapping("/all")
    public List<UserResponseDto> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/user")
    public UserResponseDto getUserById(@RequestHeader("user_id") Long id) {
        return userService.getUserById(id);
    }

    @PostMapping("/createUser")
    public UserResponseDto createUser(@Valid @RequestBody UserRequestDto userRequestDto) {
        return userService.createUser(userRequestDto);
    }

    @DeleteMapping("/deleteUser")
    public ResponseEntity<ApiResponse> deleteUserById(@RequestHeader("user_id") Long id) {
        userService.deleteUserById(id);
        return ResponseEntity.ok(ApiResponse.builder()
                .message("User deleted successfully")
                .build());
    }

    @PatchMapping("/updateUser")
    public UserResponseDto updateUserDetails(@RequestBody Map<String, Object> body) {
        Long userId = Long.parseLong(body.get("user_id").toString());
        UserRequestDto userRequestDto = objectMapper.convertValue(body, UserRequestDto.class);
        return userService.updateUserDetails(userId, userRequestDto);
    }

    @PatchMapping("/isActive")
    public ResponseEntity<ApiResponse> updateUserStatus(
            @RequestParam(name = "user_id") Long id,
            @RequestParam(name = "is_active") boolean requestedIsActive) {
        userService.updateUserStatus(id, requestedIsActive);
        return ResponseEntity.ok(ApiResponse.builder()
                .message("Status updated successfully")
                .build());
    }
}