package com.furqan.ecommerce.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CategoryRequestDto {
    private Long categoryId;
    @NotBlank
    private String name;
    private String description;
}
