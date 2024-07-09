package com.example.foneproject.exception;

public class ObjectNotFoundException extends RuntimeException {
    public ObjectNotFoundException(String email) {
        super("Cannot find data associated with the provided email: " + email);
    }

    public ObjectNotFoundException(int id) {
        super("Cannot find data associated with the provided ID: " + id);
    }
}
