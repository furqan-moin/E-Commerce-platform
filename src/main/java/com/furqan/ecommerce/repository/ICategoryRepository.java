package com.furqan.ecommerce.repository;

import com.furqan.ecommerce.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ICategoryRepository extends JpaRepository<CategoryEntity, Long> {
    boolean existsByName(String name);

    Optional<CategoryEntity> findByName(String name);

    void deleteByName(String name);
}
