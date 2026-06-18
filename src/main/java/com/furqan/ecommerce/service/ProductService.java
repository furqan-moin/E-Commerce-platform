package com.furqan.ecommerce.service;

import com.furqan.ecommerce.dto.ProductRequestDto;
import com.furqan.ecommerce.dto.ProductResponseDto;
import com.furqan.ecommerce.entity.ProductEntity;
import com.furqan.ecommerce.repository.IProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final IProductRepository productRepository;

    public List<ProductEntity> getAllProducts() {
        return productRepository.findAll();
    }

    public ProductResponseDto createProduct(ProductRequestDto productRequestDto) {
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
            throw new RuntimeException("productId is required");
        }
        ProductEntity productEntity = productRepository.findById(productId).orElseThrow(() ->
                new RuntimeException("Product not found with productId : " + productId));
        return toResponseDto(productEntity);
    }

    public ProductResponseDto getProductByName(String name) {
        if (name == null) {
            throw new RuntimeException("productName is required");
        }
        ProductEntity productEntity = productRepository.findByName(name).orElseThrow(() ->
                new RuntimeException("Product not found with name : " + name));
        return toResponseDto(productEntity);
    }

    public List<ProductResponseDto> getProuctsByCategoryId(Long categoryId) {
        if (categoryId == null) {
            throw new RuntimeException("categoryId is required");
        }
        ProductEntity productEntity = productRepository.findByCategoryId(categoryId).orElseThrow(() ->
                new RuntimeException("Product not found with categoryId : " + categoryId));
        return productRepository.findByCategoryId(categoryId).stream()
                .map(this::toResponseDto)
                .toList();
    }

    public ProductResponseDto updateProduct(Long productId, ProductRequestDto productRequestDto) {
        if (productId == null) {
            throw new RuntimeException("productId is required");
        }
        ProductEntity productEntity = productRepository.
                findById(productId).orElseThrow(() -> new RuntimeException("Product not found with productId : " + productId));
        applyRequestToEntity(productRequestDto, productEntity);
        return toResponseDto(productRepository.save(productEntity));
    }

    public void deleteProduct(Long productId) {
        if (productId == null) {
            throw new RuntimeException("productId is required");
        }
        ProductEntity productEntity = productRepository.findById(productId).orElseThrow(() ->
                new RuntimeException("Product not found with productId : " + productId));
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
