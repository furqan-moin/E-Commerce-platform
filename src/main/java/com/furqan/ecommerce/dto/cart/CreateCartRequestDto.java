package com.furqan.ecommerce.dto.cart;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateCartRequestDto {
    @NotNull
    private Long userId;
}
