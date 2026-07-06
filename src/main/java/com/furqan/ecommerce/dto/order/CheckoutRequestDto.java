package com.furqan.ecommerce.dto.order;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CheckoutRequestDto {
    @NotNull
    private Long userId;
    @NotNull
    private Long shippingAddressId;

    private Long billingAddressId;
}
