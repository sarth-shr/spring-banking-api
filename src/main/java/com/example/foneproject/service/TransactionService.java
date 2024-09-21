package com.example.foneproject.service;

import com.example.foneproject.entity.Account;
import com.example.foneproject.util.ApiResponse;
import org.springframework.http.ResponseEntity;

public interface TransactionService {
    void saveDeposit(Account account, int amount);

    void saveTransfer(Account fromAccount, Account toAccount, int amount);

    void saveInterest(Account account, int amount);

    ResponseEntity<ApiResponse> getAll(int page);

    ResponseEntity<ApiResponse> getAllByAccount(String accNumber, int page);
}
