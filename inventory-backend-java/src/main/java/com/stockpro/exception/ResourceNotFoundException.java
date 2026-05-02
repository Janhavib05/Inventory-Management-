package com.stockpro.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
    public ResourceNotFoundException(Long id) {
        super("Product not found with id: " + id);
    }
}
