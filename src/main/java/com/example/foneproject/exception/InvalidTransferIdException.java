package com.example.foneproject.exception;

public class InvalidTransferIdException extends RuntimeException {
    public InvalidTransferIdException() {
        super("Cannot transfer funds because one of the IDs provided is invalid");
    }
}
