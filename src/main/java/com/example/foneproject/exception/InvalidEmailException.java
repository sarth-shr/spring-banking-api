package com.example.foneproject.exception;

public class InvalidEmailException extends RuntimeException {
    public InvalidEmailException(String email) {
        super("User is already registered with email: " + email);
    }
}
