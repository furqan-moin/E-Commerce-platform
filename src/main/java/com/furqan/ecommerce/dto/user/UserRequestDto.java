package com.furqan.ecommerce.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserRequestDto {
    private String firstName;
    private String middleName;
    private String lastName;

    @Email
    private String email;
    @NotBlank
    private String password;

    private String phoneNumber;

    private String countryCode;

    private String language;
}
