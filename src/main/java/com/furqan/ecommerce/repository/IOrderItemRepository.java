package com.furqan.ecommerce.repository;

import com.furqan.ecommerce.entity.OrderItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IOrderItemRepository extends JpaRepository<OrderItemEntity, Long> {
    Optional<OrderItemEntity> findByOrderItemId(Long orderItemId);

    List<OrderItemEntity> findByOrderId(Long orderId);
}
