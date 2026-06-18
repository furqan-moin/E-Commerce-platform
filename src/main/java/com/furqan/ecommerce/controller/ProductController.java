package com.furqan.ecommerce.controller;


import com.furqan.ecommerce.dto.ApiResponse;
import com.furqan.ecommerce.dto.ProductRequestDto;
import com.furqan.ecommerce.dto.ProductResponseDto;
import com.furqan.ecommerce.entity.ProductEntity;
import com.furqan.ecommerce.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ecommerce/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/all")
    public List<ProductEntity> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/productId")
    public ProductResponseDto getProductById(Long id) {
        return productService.getProductById(id);
    }

    @PostMapping("/createProduct")
    public ProductResponseDto createProduct(@RequestBody ProductRequestDto productRequestDto) {
        return productService.createProduct(productRequestDto);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<ApiResponse> deleteProduct(@RequestBody Long productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.ok(ApiResponse.builder()
                .message("Product deleted successfully")
                .build());
    }

    @GetMapping("/productByName")
    public ProductResponseDto getProductByName(@RequestParam String name) {
        return productService.getProductByName(name);
    }

    @GetMapping("'productsByCategoryId")
    public List<ProductResponseDto> getProductsByCategoryId(@RequestParam Long categoryId) {
        return productService.getProuctsByCategoryId(categoryId);
    }

    @PatchMapping("/updateProduct")
    public ProductResponseDto updateProduct(@RequestBody ProductRequestDto productRequestDto) {
        return productService.updateProduct(productRequestDto.getProductId(), productRequestDto);
    }
}
