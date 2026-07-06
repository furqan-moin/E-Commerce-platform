package com.furqan.ecommerce.controller;

import com.furqan.ecommerce.dto.cart.*;
import com.furqan.ecommerce.dto.common.ApiResponse;
import com.furqan.ecommerce.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/ecommerce/v1/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @PostMapping("/createCart")
    public CartResponseDto createCart(@Valid @RequestBody CreateCartRequestDto requestDto) {
        return cartService.createCart(requestDto.getUserId());
    }

    @DeleteMapping("/removeItem")
    public ResponseEntity<ApiResponse> removeItem(@Valid @RequestBody RemoveCartItemRequestDto request) {
        cartService.removeItem(request.getCartItemId());
        return ResponseEntity.ok(ApiResponse.builder()
                .message("Cart item successfully removed")
                .build());
    }

    @PatchMapping("/updateQuantity")
    public ResponseEntity<ApiResponse> updateQuantity(@Valid @RequestBody UpdateCartItemRequestDto request) {
        cartService.updateQuantity(request);
        return ResponseEntity.ok(ApiResponse.builder()
                .message("cart item quantity updated successfully")
                .build());
    }

    @PostMapping("/addItem")
    public CartResponseDto addItem(@Valid @RequestBody AddCartItemRequestDto request) {
        return cartService.addItem(request);
    }

    @GetMapping("/getCart")
    public CartResponseDto getCart(@Valid @RequestHeader("user_id") Long userId) {
        return cartService.getCart(userId);
    }

}
