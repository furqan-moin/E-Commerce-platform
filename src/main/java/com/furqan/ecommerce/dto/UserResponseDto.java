package com.furqan.ecommerce.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UserResponseDto {
    private long userId;
    private String firstName;
    private String middleName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String countryCode;
    private Boolean isActive;
    private String language;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
