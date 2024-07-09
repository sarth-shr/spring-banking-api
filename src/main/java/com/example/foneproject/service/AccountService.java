package com.example.foneproject.service;

import com.example.foneproject.entity.Account;
import org.springframework.data.domain.Page;

public interface AccountService {
    Account get(int id);

    Page<Account> getAll(int page);

    void open(Account account);

    void deposit(int accId, int amount);

    void transfer(int fromId, int toId, int amount);

    void delete(int id);
}
