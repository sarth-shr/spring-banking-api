package com.example.foneproject.exception;

public class EmptyListException extends RuntimeException {
    public EmptyListException() {
        super("Retrieved list of data is empty");
    }
}
