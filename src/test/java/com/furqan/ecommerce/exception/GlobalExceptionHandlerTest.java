package com.furqan.ecommerce.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void addressNotFoundMapsToNotFoundResponse() {
        var response = handler.handleAddressNotFound(
                new AddressNotFoundException("Address not found"));

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getBody().getStatus());
        assertEquals("Address not found", response.getBody().getMessage());
    }
}
