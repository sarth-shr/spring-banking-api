package com.example.foneproject.exception;

public class SameTransferIdException extends RuntimeException {
    public SameTransferIdException() {
        super("Cannot transfer to the same A/C #");
    }
}
