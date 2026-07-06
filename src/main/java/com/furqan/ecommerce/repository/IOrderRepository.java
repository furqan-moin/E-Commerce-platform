package com.furqan.ecommerce.repository;

import com.furqan.ecommerce.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IOrderRepository extends JpaRepository<OrderEntity, Long> {
    List<OrderEntity> findByUserId(Long userId);

    Optional<OrderEntity> findByOrderId(Long orderId);

    Optional<OrderEntity> findByOrderIdAndUserId(Long orderId, Long userId);

    Optional<OrderEntity> findByOrderNumber(String orderNumber);
}
