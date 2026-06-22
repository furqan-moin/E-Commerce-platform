package com.furqan.ecommerce.dto.cart;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateCartItemRequestDto {
    @NotNull
    private Long cartItemId;

    @NotNull
    @Min(1)
    private Integer quantity;

}
