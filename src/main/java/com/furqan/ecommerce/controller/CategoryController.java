package com.furqan.ecommerce.controller;


import com.furqan.ecommerce.dto.category.CategoryRequestDto;
import com.furqan.ecommerce.dto.category.CategoryResponseDto;
import com.furqan.ecommerce.dto.common.ApiResponse;
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

    @DeleteMapping("/deleteCategoryById")
    public ResponseEntity<ApiResponse> deleteCategoryById(@RequestHeader("category_id") Long id) {
        categoryService.deleteCategoryById(id);
        return ResponseEntity.ok(ApiResponse.builder()
                .message("Category deleted successfully")
                .build());
    }

    @GetMapping("/getCategory")
    public CategoryResponseDto getCategoryByName(@RequestHeader("category_name") String name) {
        return categoryService.getCategoryByName(name);
    }

    @DeleteMapping("/deleteCategoryByName")
    public ResponseEntity<ApiResponse> deleteCategoryByName(@RequestHeader("category_name") String name) {
        categoryService.deleteCategoryByName(name);
        return ResponseEntity.ok(ApiResponse.builder()
                .message("Category deleted successfully")
                .build());
    }

    @PatchMapping("/updateCategory")
    public CategoryResponseDto updateCategory(@Valid @RequestBody CategoryRequestDto categoryRequestDto) {
        return categoryService.updateCategory(categoryRequestDto);
    }

}
