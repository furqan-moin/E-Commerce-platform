package com.furqan.ecommerce.dto.order;


import com.furqan.ecommerce.enums.OrderStatus;
import com.furqan.ecommerce.enums.PaymentStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class OrderResponseDto {
    private Long orderId;
    private String orderNumber;
    private Long userId;
    private OrderStatus status;
    private PaymentStatus paymentStatus;
    private BigDecimal totalAmount;
    private Long shippingAddressId;
    private Long billingAddressId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<OrderItemResponseDto> items;

}
