package com.furqan.ecommerce.dto.product;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductRequestDto {
    private Long productId;
    @NotNull
    private Long categoryId;
    @NotBlank
    private String name;
    private String description;
    @NotNull
    private BigDecimal price;
    @Min(0)
    private Integer stockQuantity;

}
