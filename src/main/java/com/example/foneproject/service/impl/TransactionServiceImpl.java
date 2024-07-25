package com.example.foneproject.service.impl;

import com.example.foneproject.entity.Account;
import com.example.foneproject.entity.Transaction;
import com.example.foneproject.repository.TransactionRepository;
import com.example.foneproject.service.TransactionService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;

    @Override
    public void saveDeposit(Account account, int amount) {
        Transaction transaction = Transaction.builder()
                .type("DEPOSIT")
                .toAccount(account)
                .toAccOldBalance(account.getBalance() - amount)
                .toAccNewBalance(account.getBalance())
                .amount(amount)
                .date(new Date())
                .build();

        transactionRepository.save(transaction);
    }

    @Override
    public void saveTransfer(Account fromAccount, Account toAccount, int amount) {
        Transaction transaction = Transaction.builder()
                .type("TRANSFER")
                .fromAccount(fromAccount)
                .fromAccOldBalance(fromAccount.getBalance() + amount)
                .fromAccNewBalance(fromAccount.getBalance())
                .toAccount(toAccount)
                .toAccOldBalance(toAccount.getBalance() - amount)
                .toAccNewBalance(toAccount.getBalance())
                .toAccount(toAccount)
                .amount(amount)
                .date(new Date())
                .build();

        transactionRepository.save(transaction);
    }

    @Override
    public void saveInterest(Account account, int amount) {
        Transaction transaction = Transaction.builder()
                .type("INTEREST RECEIVED")
                .toAccount(account)
                .toAccOldBalance(account.getBalance() - amount)
                .toAccNewBalance(account.getBalance())
                .amount(amount)
                .date(new Date())
                .build();

        transactionRepository.save(transaction);
    }

    @Override
    public Page<Transaction> getAll(int page) {
        List<Transaction> transactions = transactionRepository.findAll();
        Pageable pageable = PageRequest.of(page, 3);
        return transactionRepository.findAll(pageable);

    }

    @Override
    public Page<Transaction> getByAccount(int page, int accId) {
        List<Transaction> transactions = transactionRepository.findAll();
        if (transactions.isEmpty()) {
            throw new EntityNotFoundException();
        }
        Pageable pageable = PageRequest.of(page, 3);
        return transactionRepository.findByAccountPageable(pageable, accId);

    }
}
