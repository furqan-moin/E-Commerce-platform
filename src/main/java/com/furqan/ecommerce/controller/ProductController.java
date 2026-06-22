package com.furqan.ecommerce.controller;


import com.furqan.ecommerce.dto.common.ApiResponse;
import com.furqan.ecommerce.dto.product.ProductRequestDto;
import com.furqan.ecommerce.dto.product.ProductResponseDto;
import com.furqan.ecommerce.service.ProductService;
import jakarta.validation.Valid;
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
    public List<ProductResponseDto> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/productId")
    public ProductResponseDto getProductById(@RequestHeader("product_id") Long id) {
        return productService.getProductById(id);
    }

    @PostMapping("/addProduct")
    public ProductResponseDto addProduct(@Valid @RequestBody ProductRequestDto productRequestDto) {
        return productService.addProduct(productRequestDto);
    }

    @DeleteMapping("/productId")
    public ResponseEntity<ApiResponse> deleteProduct(@Valid @RequestHeader("product_id") Long productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.ok(ApiResponse.builder()
                .message("Product deleted successfully")
                .build());
    }

    @GetMapping("/productByName")
    public ProductResponseDto getProductByName(@RequestParam String name) {
        return productService.getProductByName(name);
    }

    @GetMapping("/productsByCategoryId")
    public List<ProductResponseDto> getProductsByCategoryId(@RequestParam Long categoryId) {
        return productService.getProductsByCategoryId(categoryId);
    }

    @PatchMapping("/updateProduct")
    public ProductResponseDto updateProduct(@Valid @RequestBody ProductRequestDto productRequestDto) {
        return productService.updateProduct(productRequestDto.getProductId(), productRequestDto);
    }

}
