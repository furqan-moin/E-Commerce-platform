package com.furqan.ecommerce.controller;


import com.furqan.ecommerce.dto.*;
import com.furqan.ecommerce.entity.AddressEntity;
import com.furqan.ecommerce.entity.CategoryEntity;
import com.furqan.ecommerce.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ecommerce/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/all")
    public List<CategoryEntity> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @GetMapping("/categoryId")
    public CategoryResponseDto getCategoryById(@RequestHeader("category_id") Long categoryId) {
        return categoryService.getCategoryById(categoryId);
    }

    @PostMapping("/createCategory")
    public CategoryResponseDto createCategory(@Valid @RequestBody CategoryRequestDto categoryRequestDto) {
        return categoryService.createCategory(categoryRequestDto);
    }

    @DeleteMapping("/deleteCategory")
    public ResponseEntity<ApiResponse> deleteCategoryById(@RequestHeader("category_id") Long id) {
        categoryService.deleteCategoryById(id);
        return ResponseEntity.ok(ApiResponse.builder()
                .message("Address deleted successfully")
                .build());
    }

    @PatchMapping("/updateCategory")
    public CategoryResponseDto updateCategory(@Valid @RequestBody CategoryRequestDto categoryRequestDto) {
        return categoryService.updateCategory(categoryRequestDto);
    }

}
