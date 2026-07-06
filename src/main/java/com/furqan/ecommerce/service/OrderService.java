package com.furqan.ecommerce.service;

import com.furqan.ecommerce.dto.address.AddressResponseDto;
import com.furqan.ecommerce.dto.cart.CartItemResponseDto;
import com.furqan.ecommerce.dto.cart.CartResponseDto;
import com.furqan.ecommerce.dto.order.CheckoutRequestDto;
import com.furqan.ecommerce.dto.order.OrderItemResponseDto;
import com.furqan.ecommerce.dto.order.OrderResponseDto;
import com.furqan.ecommerce.dto.user.UserResponseDto;
import com.furqan.ecommerce.entity.OrderEntity;
import com.furqan.ecommerce.entity.OrderItemEntity;
import com.furqan.ecommerce.entity.ProductEntity;
import com.furqan.ecommerce.enums.OrderStatus;
import com.furqan.ecommerce.enums.PaymentStatus;
import com.furqan.ecommerce.exception.*;
import com.furqan.ecommerce.repository.ICartItemRepository;
import com.furqan.ecommerce.repository.IOrderItemRepository;
import com.furqan.ecommerce.repository.IOrderRepository;
import com.furqan.ecommerce.repository.IProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {
    private final IOrderRepository orderRepository;
    private final IOrderItemRepository orderItemRepository;
    private final IProductRepository productRepository;
    private final ICartItemRepository cartItemRepository;

    private final UserService userService;
    private final AddressService addressService;
    private final CartService cartService;

    public OrderResponseDto placeOrder(CheckoutRequestDto request) {
        return placeOrder(
                request.getUserId(),
                request.getShippingAddressId(),
                request.getBillingAddressId()
        );
    }

    public List<OrderResponseDto> getOrdersByUserId(Long userId) {
        validateUser(userId);
        return orderRepository.findByUserId(userId).stream()
                .map(order -> toOrderResponseDto(order, List.of()))
                .toList();
    }

    public OrderResponseDto cancelOrder(Long orderId, Long userId) {
        OrderEntity order = findOrderForUser(orderId, userId);
        if (order.getStatus() != OrderStatus.CREATED) {
            throw new InvalidOrderStateException("Order cannot be cancelled in status: " + order.getStatus());
        }
        List<OrderItemEntity> items = orderItemRepository.findByOrderId(orderId);
        for (OrderItemEntity item : items) {
            ProductEntity product = loadProduct(item.getProductId());
            product.setStockQuantity(product.getStockQuantity() +
                    item.getQuantity());
            productRepository.save(product);
        }
        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
        return toOrderResponseDto(order, buildItemResponses(orderId));
    }

    public OrderResponseDto getOrderById(Long orderId, Long userId) {
        OrderEntity order = findOrderForUser(orderId, userId);
        return toOrderResponseDto(order, buildItemResponses(order.getOrderId()));
    }


    private OrderResponseDto placeOrder(Long userId, Long shippingAddressId, Long billingAddressId) {
        if (userId == null || shippingAddressId == null) {
            throw new IllegalArgumentException("UserId and ShippingAddressId are mandatory");
        }
        validateUser(userId);
        Long resolvedBillingAddressId = billingAddressId != null ? billingAddressId : shippingAddressId;
        validateAddress(shippingAddressId, userId, "shipping");
        if (!resolvedBillingAddressId.equals(shippingAddressId)) {
            validateAddress(billingAddressId, userId, "billing");
        }
        CartResponseDto cart = cartService.getCart(userId);
        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            throw new EmptyCartException("Can't place order with an empty cart");
        }

        BigDecimal totalAmount = BigDecimal.ZERO;
        for (CartItemResponseDto cartItem : cart.getItems()) {
            ProductEntity product = loadProduct(cartItem.getProductId());
            validateStock(product, cartItem.getQuantity());
            totalAmount = totalAmount.add(product.getPrice().multiply(new BigDecimal(cartItem.getQuantity())));
        }
        OrderEntity order = orderRepository.save(OrderEntity.builder()
                .orderNumber(generateOrderNumber())
                .userId(userId)
                .status(OrderStatus.CREATED)
                .paymentStatus(PaymentStatus.PENDING)
                .totalAmount(totalAmount)
                .shippingAddressId(shippingAddressId)
                .billingAddressId(resolvedBillingAddressId)
                .build());

        List<OrderItemResponseDto> itemResponses = new ArrayList<>();
        for (CartItemResponseDto cartItem : cart.getItems()) {
            ProductEntity product = loadProduct(cartItem.getProductId());
            OrderItemEntity savedItem = orderItemRepository.save(
                    OrderItemEntity.builder()
                            .orderId(order.getOrderId())
                            .productId(product.getProductId())
                            .quantity(cartItem.getQuantity())
                            .price(product.getPrice())
                            .build());
            product.setStockQuantity(product.getStockQuantity() -
                    cartItem.getQuantity());
            productRepository.save(product);

            itemResponses.add(toOrderItemResponseDto(savedItem, product.getName()));
            cartItemRepository.deleteById(cartItem.getCartItemId());
        }
        return toOrderResponseDto(order, itemResponses);
    }

    private OrderResponseDto toOrderResponseDto(OrderEntity order, List<OrderItemResponseDto> items) {
        return OrderResponseDto.builder()
                .orderId(order.getOrderId())
                .orderNumber(order.getOrderNumber())
                .userId(order.getUserId())
                .status(order.getStatus())
                .paymentStatus(order.getPaymentStatus())
                .totalAmount(order.getTotalAmount())
                .shippingAddressId(order.getShippingAddressId())
                .billingAddressId(order.getBillingAddressId())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .items(items)
                .build();
    }

    private OrderEntity findOrderForUser(Long orderId, Long userId) {
        return orderRepository.findByOrderIdAndUserId(orderId, userId)
                .orElseThrow(() -> new OrderNotFoundException(
                        "Order not found with orderId: " + orderId + " for userId: " + userId
                ));
    }

    private OrderItemResponseDto toOrderItemResponseDto(OrderItemEntity item, String productName) {
        BigDecimal lineTotal = item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
        return OrderItemResponseDto.builder()
                .orderItemId(item.getOrderItemId())
                .productId(item.getProductId())
                .productName(productName)
                .quantity(item.getQuantity())
                .price(item.getPrice())
                .totalPrice(lineTotal)
                .createdAt(item.getCreatedAt())
                .build();
    }

    private String generateOrderNumber() {
        String date = LocalDateTime.now().format(
                DateTimeFormatter.ofPattern("yyyyMMdd"));
        return "ORD-" + date + "-" + String.format("%04d", orderRepository.count() + 1);
    }

    private void validateStock(ProductEntity product, int quantity) {
        if (quantity > product.getStockQuantity()) {
            throw new OutOfStockException("Only " + product.getStockQuantity() + " items available for product: " + product.getName());
        }
    }

    private ProductEntity loadProduct(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() ->
                        new ProductNotFoundException("Product with id " + productId + " not found"));
    }

    private void validateAddress(Long addressId, Long userId, String type) {
        AddressResponseDto address = addressService.getAddressByAddressId(addressId);
        if (!userId.equals(address.getUserId())) {
            throw new AddressNotBelongsToUserException(type + " address does not belong to user");
        }
    }

    private void validateUser(Long userId) {
        UserResponseDto user = userService.getUserById(userId);
        if (Boolean.FALSE.equals(user.getIsActive())) {
            throw new InactiveUserException("User is not active: " + userId);
        }
    }

    private List<OrderItemResponseDto> buildItemResponses(Long orderId) {
        return orderItemRepository.findByOrderId(orderId).stream()
                .map(item -> {
                    ProductEntity product = loadProduct(item.getProductId());
                    return toOrderItemResponseDto(item, product.getName());
                })
                .toList();
    }
}