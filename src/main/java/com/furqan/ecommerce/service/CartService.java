package com.furqan.ecommerce.service;

import com.furqan.ecommerce.dto.cart.*;
import com.furqan.ecommerce.entity.CartEntity;
import com.furqan.ecommerce.entity.CartItemEntity;
import com.furqan.ecommerce.entity.ProductEntity;
import com.furqan.ecommerce.exception.*;
import com.furqan.ecommerce.repository.ICartItemRepository;
import com.furqan.ecommerce.repository.ICartRepository;
import com.furqan.ecommerce.repository.IProductRepository;
import com.furqan.ecommerce.repository.IUserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {
    final ICartRepository cartRepository;
    private final IProductRepository productRepository;
    private final IUserRepository userRepository;
    private final ICartItemRepository cartItemRepository;

    public CartResponseDto createCart(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("userId is required");
        }
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("User not found with the userId: " + userId);
        }
        CartEntity cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> cartRepository.save(CartEntity.builder()
                        .userId(userId).isActive(true).build()));
        return toResponseDto(cart);
    }

    public void removeItem(Long cartItemId) {
        if (cartItemId == null) {
            throw new IllegalArgumentException("cartItemId is required");
        }
        CartItemEntity cartItem = cartItemRepository.findByCartItemId(cartItemId)
                .orElseThrow(() ->
                        new CartItemNotFoundException("cart item not found with id " + cartItemId));
        cartItemRepository.delete(cartItem);
    }

    public void updateQuantity(
            UpdateCartItemRequestDto request) {
        CartItemEntity cartItem = cartItemRepository.findByCartItemId(request.getCartItemId())
                .orElseThrow(() -> new CartItemNotFoundException("cart item not found with id " + request.getCartItemId()));

        ProductEntity productEntity = productRepository.findById(cartItem.getProductId())
                .orElseThrow(() -> new ProductNotFoundException("product not found with id " + cartItem.getProductId()));

        if (request.getQuantity() > productEntity.getStockQuantity()) {
            throw new OutOfStockException("Only " + productEntity.getStockQuantity() + " items available for product: " + productEntity.getName());
        }
        cartItem.setQuantity(request.getQuantity());
        cartItemRepository.save(cartItem);
    }

    public CartResponseDto addItem(AddCartItemRequestDto request) {

        userRepository.findById(request.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found with userId: " + request.getUserId()));

        ProductEntity product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ProductNotFoundException("product not found with id " + request.getProductId()));

        CartResponseDto cart = createCart(request.getUserId());

        Optional<CartItemEntity> existingCartItem =
                cartItemRepository.findByCartIdAndProductId(cart.getCartId(), request.getProductId());

        int newQuantity = existingCartItem.isPresent()
                ? existingCartItem.get().getQuantity() + request.getQuantity()
                : request.getQuantity();

        if (newQuantity > product.getStockQuantity()) {
            throw new OutOfStockException(
                    "Only " + product.getStockQuantity() + " items available for product: " + product.getName());
        }

        if (existingCartItem.isPresent()) {
            CartItemEntity cartItem = existingCartItem.get();
            cartItem.setQuantity(newQuantity);
            cartItemRepository.save(cartItem);
        } else {
            cartItemRepository.save(CartItemEntity.builder()
                    .cartId(cart.getCartId())
                    .productId(request.getProductId())
                    .quantity(request.getQuantity())
                    .build()
            );
        }
        return getCart(request.getUserId());
    }

    public CartResponseDto getCart(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("User not found with userId: " + userId);
        }
        CartEntity cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new CartNotFoundException("Cart not found with userId: " + userId));
        List<CartItemEntity> cartItems = cartItemRepository.findByCartId(cart.getCartId());
        List<CartItemResponseDto> itemResponses = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (CartItemEntity cartItem : cartItems) {
            ProductEntity product =
                    productRepository.findById(cartItem.getProductId())
                            .orElseThrow(() -> new ProductNotFoundException("product not found with id " + cartItem.getProductId()));
            BigDecimal itemTotal = product.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity()));
            totalAmount = totalAmount.add(itemTotal);

            CartItemResponseDto itemResponse =
                    CartItemResponseDto.builder().cartItemId(cartItem.getCartItemId())
                            .productId(product.getProductId()).productName(product.getName())
                            .price(product.getPrice()).quantity(cartItem.getQuantity())
                            .totalPrice(itemTotal)
                            .createdAt(cartItem.getCreatedAt())
                            .updatedAt(cartItem.getUpdatedAt())
                            .build();
            itemResponses.add(itemResponse);
        }
        return CartResponseDto.builder()
                .cartId(cart.getCartId()).userId(cart.getUserId()).isActive(cart.getIsActive())
                .totalAmount(totalAmount).items(itemResponses)
                .createdAt(cart.getCreatedAt())
                .updatedAt(cart.getUpdatedAt())
                .build();
    }

    private CartResponseDto toResponseDto(CartEntity cart) {
        return CartResponseDto.builder()
                .cartId(cart.getCartId()).userId(cart.getUserId())
                .items(List.of()).totalAmount(BigDecimal.ZERO).build();
    }
}
