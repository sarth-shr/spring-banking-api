package com.example.foneproject.exception;

public class ActiveBalanceException extends RuntimeException {
    public ActiveBalanceException(String accNumber, int amount) {
        super("A/C #" + accNumber + " has an active balance of: " + amount + ". Make sure the balance is 0 before closing the account");
    }
}
