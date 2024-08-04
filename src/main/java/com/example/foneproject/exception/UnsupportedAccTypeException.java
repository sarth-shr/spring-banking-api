package com.example.foneproject.exception;

public class UnsupportedAccTypeException extends RuntimeException {
    public UnsupportedAccTypeException() {
        super("Only supported account types are: savings and checking");
    }
}
