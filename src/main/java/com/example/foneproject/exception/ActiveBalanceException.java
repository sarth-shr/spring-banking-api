package com.example.foneproject.exception;

public class ActiveBalanceException extends RuntimeException {
    public ActiveBalanceException(int id, int amount) {
        super("Account with ID: " + id + " has an active balance of: " + amount + ". Make sure the balance is 0 before closing the account");
    }
}
