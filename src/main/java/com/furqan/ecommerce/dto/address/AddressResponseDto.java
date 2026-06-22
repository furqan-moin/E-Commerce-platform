package com.furqan.ecommerce.dto.address;

import com.furqan.ecommerce.enums.AddressType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddressResponseDto {
    private Long addressId;
    private Long userId;
    private AddressType addressType;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String pinCode;
    private String country;
    private String countryCode;
}
