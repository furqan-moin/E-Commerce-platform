package com.furqan.ecommerce.dto.common;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiResponse {
    private String message;
}
