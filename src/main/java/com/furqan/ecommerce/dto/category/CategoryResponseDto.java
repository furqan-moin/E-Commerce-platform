package com.furqan.ecommerce.dto.category;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategoryResponseDto {
    private Long categoryId;
    private String name;
    private String description;
}
