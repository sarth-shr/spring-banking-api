package com.example.foneproject.service;

import com.example.foneproject.entity.Account;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface TransactionService {
    void saveDeposit(Account account, int amount);

    void saveTransfer(Account fromAccount, Account toAccount, int amount);

    void saveInterest(Account account, int amount);

    ResponseEntity<Map<String, Object>> getAll(int page);

    ResponseEntity<Map<String, Object>> getByAccount(int page, int accId);
}
