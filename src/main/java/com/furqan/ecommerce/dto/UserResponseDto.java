package com.furqan.ecommerce.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponseDto {
    private  long userId;
    private  String name;
    private  String email;
    private String phoneNumber;
    private Boolean isActive;
    private String language;
}
