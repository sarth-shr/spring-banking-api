package com.example.foneproject.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String email) {
        super("Resource not found: " + email);
    }

    public ResourceNotFoundException(int id) {
        super("Resource not found: " + id);
    }
}
