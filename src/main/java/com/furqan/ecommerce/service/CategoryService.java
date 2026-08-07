package com.furqan.ecommerce.service;

import com.furqan.ecommerce.dto.category.CategoryRequestDto;
import com.furqan.ecommerce.dto.category.CategoryResponseDto;
import com.furqan.ecommerce.entity.CategoryEntity;
import com.furqan.ecommerce.exception.CategoryNotFoundException;
import com.furqan.ecommerce.repository.ICategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final ICategoryRepository categoryRepository;

    public List<CategoryEntity> getAllCategories() {
        return categoryRepository.findAll();
    }

    public CategoryResponseDto createCategory(CategoryRequestDto categoryRequestDto) {
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setName(categoryRequestDto.getName());
        categoryEntity.setDescription(categoryRequestDto.getDescription());
        applyRequestToEntity(categoryRequestDto, categoryEntity);
        return toResponseDto(categoryRepository.save(categoryEntity));
    }

    public CategoryResponseDto getCategoryById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("categoryId is required");
        }
        CategoryEntity categoryEntity = categoryRepository.findById(id).orElseThrow(() ->
                new CategoryNotFoundException("Category not found with id: " + id));
        return toResponseDto(categoryEntity);
    }

    public CategoryResponseDto getCategoryByName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("categoryName is required");
        }
        CategoryEntity categoryEntity = categoryRepository.findByName(name).orElseThrow(() ->
                new CategoryNotFoundException("Category not found with name: " + name));
        return toResponseDto(categoryEntity);
    }

    public void deleteCategoryById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("categoryId is required");
        }
        categoryRepository.findById(id).orElseThrow(() ->
                new CategoryNotFoundException("Category not found with id: " + id));
        categoryRepository.deleteById(id);
    }

    public void deleteCategoryByName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("categoryName is required");
        }
        categoryRepository.findByName(name).orElseThrow(() ->
                new CategoryNotFoundException("Category not found with name: " + name));
        categoryRepository.deleteByName(name);
    }

    public CategoryResponseDto updateCategory(CategoryRequestDto categoryRequestDto) {
        if (categoryRequestDto.getCategoryId() == null) {
            throw new IllegalArgumentException("categoryId is required");
        }
        CategoryEntity categoryEntity = categoryRepository
                .findById(categoryRequestDto.getCategoryId())
                .orElseThrow(() -> new CategoryNotFoundException(
                        "Category not found with id: " + categoryRequestDto.getCategoryId()));
        applyRequestToEntity(categoryRequestDto, categoryEntity);
        return toResponseDto(categoryRepository.save(categoryEntity));

    }


    private CategoryResponseDto toResponseDto(CategoryEntity categoryEntity) {
        return CategoryResponseDto.builder()
                .categoryId(categoryEntity.getCategoryId())
                .name(categoryEntity.getName())
                .description(categoryEntity.getDescription())
                .build();
    }

    private void applyRequestToEntity(CategoryRequestDto categoryRequestDto, CategoryEntity categoryEntity) {
        categoryEntity.setName(categoryRequestDto.getName());
        categoryEntity.setDescription(categoryRequestDto.getDescription());
    }


}
