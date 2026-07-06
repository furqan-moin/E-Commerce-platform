package com.furqan.ecommerce.controller;


import com.furqan.ecommerce.dto.order.CheckoutRequestDto;
import com.furqan.ecommerce.dto.order.OrderResponseDto;
import com.furqan.ecommerce.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ecommerce/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;


    @PostMapping("/checkout")
    public OrderResponseDto placeOrder(@Valid @RequestBody CheckoutRequestDto request) {
        return orderService.placeOrder(request);
    }


    @GetMapping
    public List<OrderResponseDto> getOrdersByUserId(@RequestHeader("user_id") Long userId) {
        return orderService.getOrdersByUserId(userId);
    }

    @GetMapping("/{orderId}")
    public OrderResponseDto getOrderById(@PathVariable Long orderId, @RequestHeader("user_id") Long userId) {
        return orderService.getOrderById(orderId, userId);
    }

    @PatchMapping("/{orderId}/cancel")
    public OrderResponseDto cancelOrder(@PathVariable Long orderId, @RequestHeader("user_id") Long userId) {
        return orderService.cancelOrder(orderId, userId);
    }
}
