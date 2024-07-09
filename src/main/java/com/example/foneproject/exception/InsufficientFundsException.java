package com.example.foneproject.exception;

public class InsufficientFundsException extends RuntimeException {
    public InsufficientFundsException() {
        super("Insufficient funds to perform a balance transfer");
    }
}
