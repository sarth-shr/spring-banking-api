package com.example.foneproject.exception;

public class SameTransferIdException extends RuntimeException {
    public SameTransferIdException() {
        super("Cannot transfer to an account with the same ID");
    }
}
