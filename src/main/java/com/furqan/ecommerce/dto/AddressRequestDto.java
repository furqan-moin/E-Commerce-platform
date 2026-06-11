package com.furqan.ecommerce.dto;

import com.furqan.ecommerce.enums.AddressType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddressRequestDto {
    private Long addressId;
    @NotNull
    private Long userId;
    @NotNull
    private AddressType addressType;

    @NotBlank
    private String addressLine1;
    private String addressLine2;

    private String city;
    private String state;
    private String pinCode;
    private String country;
    private String countryCode;

}
