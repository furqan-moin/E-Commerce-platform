package com.furqan.ecommerce.dto.cart;


import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class CartResponseDto {
    private Long cartId;
    private Long userId;
    private Boolean isActive;
    private BigDecimal totalAmount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<CartItemResponseDto> items;

}
