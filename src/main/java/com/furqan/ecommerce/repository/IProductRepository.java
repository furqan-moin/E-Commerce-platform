package com.furqan.ecommerce.repository;

import com.furqan.ecommerce.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IProductRepository extends JpaRepository<ProductEntity, Long> {

    Optional<ProductEntity> findByCategoryId(Long categoryId);
    Optional<ProductEntity> findByName(String name);
}
