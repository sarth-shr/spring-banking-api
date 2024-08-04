package com.example.foneproject.exception;

public class DuplicateAccTypeException extends RuntimeException {
    public DuplicateAccTypeException(String type) {
        super("Account of type: " + type + " already exists");
    }
}
