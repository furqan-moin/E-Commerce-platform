package com.furqan.ecommerce.exception;

public class AddressNotBelongsToUserException extends RuntimeException {
    public AddressNotBelongsToUserException(String message) {
        super(message);
    }
}
