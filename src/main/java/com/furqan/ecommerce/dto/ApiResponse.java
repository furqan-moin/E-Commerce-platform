package com.furqan.ecommerce.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiResponse {
    private String message;
}
