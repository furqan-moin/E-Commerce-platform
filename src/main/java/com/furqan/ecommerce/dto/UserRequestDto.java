package com.furqan.ecommerce.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserRequestDto {
    @NotBlank
    private String firstName;
    private String middleName;
    private String lastName;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;

    private String phoneNumber;

    @NotBlank
    private String countryCode;

    private String language;
}
