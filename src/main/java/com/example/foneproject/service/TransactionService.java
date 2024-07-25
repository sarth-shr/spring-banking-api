package com.example.foneproject.service;

import com.example.foneproject.entity.Account;
import com.example.foneproject.entity.Transaction;
import org.springframework.data.domain.Page;

public interface TransactionService {
    void saveDeposit(Account account, int amount);

    void saveTransfer(Account fromAccount, Account toAccount, int amount);

    void saveInterest(Account account, int amount);

    Page<Transaction> getAll(int page);

    Page<Transaction> getByAccount(int page, int accId);
}
