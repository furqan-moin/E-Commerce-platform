package com.furqan.ecommerce.service;

import com.furqan.ecommerce.dto.product.ProductRequestDto;
import com.furqan.ecommerce.dto.product.ProductResponseDto;
import com.furqan.ecommerce.entity.ProductEntity;
import com.furqan.ecommerce.exception.CategoryNotFoundException;
import com.furqan.ecommerce.exception.ProductNotFoundException;
import com.furqan.ecommerce.repository.ICategoryRepository;
import com.furqan.ecommerce.repository.IProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor

public class ProductService {

    private final IProductRepository productRepository;
    private final ICategoryRepository categoryRepository;

    public List<ProductResponseDto> getAllProducts() {
        return productRepository.findAll().stream().map(this::toResponseDto).toList();
    }

    public ProductResponseDto addProduct(ProductRequestDto productRequestDto) {
        if (!categoryRepository.existsById(productRequestDto.getCategoryId())) {
            throw new CategoryNotFoundException(
                    "Category not found with id: " + productRequestDto.getCategoryId());
        }
        ProductEntity productEntity = new ProductEntity();
        productEntity.setCategoryId(productRequestDto.getCategoryId());
        productEntity.setName(productRequestDto.getName());
        productEntity.setDescription(productRequestDto.getDescription());
        productEntity.setPrice(productRequestDto.getPrice());
        productEntity.setStockQuantity(productRequestDto.getStockQuantity());
        return toResponseDto(productRepository.save(productEntity));
    }

    public ProductResponseDto getProductById(Long productId) {
        if (productId == null) {
            throw new IllegalArgumentException("productId is required");
        }
        ProductEntity productEntity = productRepository.findById(productId).orElseThrow(() ->
                new ProductNotFoundException("Product not found with productId : " + productId));
        return toResponseDto(productEntity);
    }

    public ProductResponseDto getProductByName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("productName is required");
        }
        ProductEntity productEntity = productRepository.findByName(name).orElseThrow(() ->
                new ProductNotFoundException("Product not found with name : " + name));
        return toResponseDto(productEntity);
    }

    public List<ProductResponseDto> getProductsByCategoryId(Long categoryId) {
        if (categoryId == null) {
            throw new IllegalArgumentException("categoryId is required");
        }

        if (!categoryRepository.existsById(categoryId)) {
            throw new CategoryNotFoundException("Category not found with id: " + categoryId);
        }
        return productRepository.findByCategoryId(categoryId).stream()
                .map(this::toResponseDto)
                .toList();
    }

    public ProductResponseDto updateProduct(Long productId, ProductRequestDto productRequestDto) {
        if (productId == null) {
            throw new IllegalArgumentException("productId is required");
        }
        if (productRequestDto.getCategoryId() != null
                && !categoryRepository.existsById(productRequestDto.getCategoryId())) {
            throw new CategoryNotFoundException(
                    "Category not found with id: " + productRequestDto.getCategoryId());
        }
        ProductEntity productEntity = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with productId : " + productId));

        applyRequestToEntity(productRequestDto, productEntity);
        return toResponseDto(productRepository.save(productEntity));
    }

    public void deleteProduct(Long productId) {
        if (productId == null) {
            throw new IllegalArgumentException("productId is required");
        }
        productRepository.findById(productId).orElseThrow(() ->
                new ProductNotFoundException("Product not found with productId : " + productId));
        productRepository.deleteById(productId);
    }

    private ProductResponseDto toResponseDto(ProductEntity productEntity) {
        return ProductResponseDto.builder()
                .productId(productEntity.getProductId())
                .categoryId(productEntity.getCategoryId())
                .name(productEntity.getName())
                .description(productEntity.getDescription())
                .price(productEntity.getPrice())
                .stockQuantity(productEntity.getStockQuantity())
                .build();
    }

    private void applyRequestToEntity(ProductRequestDto productRequestDto, ProductEntity productEntity) {
        productEntity.setCategoryId(productRequestDto.getCategoryId());
        productEntity.setName(productRequestDto.getName());
        productEntity.setDescription(productRequestDto.getDescription());
        productEntity.setPrice(productRequestDto.getPrice());
        productEntity.setStockQuantity(productRequestDto.getStockQuantity());

    }
}
