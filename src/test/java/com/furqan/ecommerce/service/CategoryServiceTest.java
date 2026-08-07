package com.furqan.ecommerce.service;

import com.furqan.ecommerce.exception.CategoryNotFoundException;
import com.furqan.ecommerce.repository.ICategoryRepository;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CategoryServiceTest {

    private final ICategoryRepository categoryRepository = mock(ICategoryRepository.class);
    private final CategoryService categoryService = new CategoryService(categoryRepository);

    @Test
    void missingCategoryUsesDomainException() {
        when(categoryRepository.findById(42L)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class,
                () -> categoryService.getCategoryById(42L));
    }

    @Test
    void blankCategoryNameUsesBadRequestException() {
        assertThrows(IllegalArgumentException.class,
                () -> categoryService.getCategoryByName(" "));
    }
}
