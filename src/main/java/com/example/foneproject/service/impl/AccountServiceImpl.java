package com.example.foneproject.service.impl;

import com.example.foneproject.entity.Account;
import com.example.foneproject.entity.Customer;
import com.example.foneproject.exception.*;
import com.example.foneproject.repository.AccountRepository;
import com.example.foneproject.repository.CustomerRepository;
import com.example.foneproject.service.AccountService;
import com.example.foneproject.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;
    private final TransactionService transactionService;

    @Override
    public Account get(int id) {
        Optional<Account> accountOptional = accountRepository.findById(id);
        if (accountOptional.isEmpty()) {
            throw new ResourceNotFoundException(id);
        }
        return accountOptional.get();
    }

    @Override
    public Page<Account> getAll(int page) {
        List<Account> accounts = accountRepository.findAll();
        Pageable pageRequest = PageRequest.of(page, 3);
        return accountRepository.findAll(pageRequest);
    }

    @Override
    public void open(Account account) {
        String email = account.getCustomer().getEmail();
        Optional<Customer> customerOptional = customerRepository.findByEmail(email);
        if (customerOptional.isEmpty()) {
            throw new ResourceNotFoundException(email);
        }
        Customer registeredCustomer = customerOptional.get();
        int initialDeposit = registeredCustomer.getInitialDeposit();
        int openingBalance = account.getBalance();

        String accType = account.getType();
        if (!(accType.equals("saving") || accType.equals("checking"))) {
            throw new UnsupportedAccTypeException();
        }

        if (accType.equals("saving")) {
            account.setInterest(5.5f);
        } else {
            account.setInterest(null);
        }

        List<Account> assoicatedEmailsList = accountRepository.findByCustomer_Email(registeredCustomer.getEmail());
        if (assoicatedEmailsList.isEmpty()) {
            account.setBalance(initialDeposit + openingBalance);
            accountRepository.save(account);
        } else {
            account.setBalance(openingBalance);
            accountRepository.save(account);
        }
    }

    @Override
    public void deposit(int accId, int amount) {
        Optional<Account> accountOptional = accountRepository.findById(accId);
        if (accountOptional.isEmpty()) {
            throw new ResourceNotFoundException(accId);
        }
        Account account = accountOptional.get();
        int currentBalance = account.getBalance();
        account.setBalance(currentBalance + amount);

        accountRepository.save(account);
        transactionService.saveDeposit(account, amount);
    }

    @Override
    public void transfer(int fromId, int toId, int amount) {
        if (fromId == toId) {
            throw new SameTransferIdException();
        }

        Optional<Account> fromAccountOptional = accountRepository.findById(fromId);
        Optional<Account> toAccountOptional = accountRepository.findById(toId);
        if (fromAccountOptional.isEmpty() || toAccountOptional.isEmpty()) {
            throw new InvalidTransferIdException();
        }

        Account fromAccount = fromAccountOptional.get();
        int fromAccountOldBalance = fromAccount.getBalance();

        if (fromAccountOldBalance < amount) {
            throw new InsufficientFundsException();
        }
        fromAccount.setBalance(fromAccountOldBalance - amount);
        accountRepository.save(fromAccount);

        Account toAccount = toAccountOptional.get();
        int toAccountOldBalance = toAccount.getBalance();
        toAccount.setBalance(toAccountOldBalance + amount);
        accountRepository.save(toAccount);

        transactionService.saveTransfer(fromAccount, toAccount, amount);
    }

    @Override
    public void delete(int id) {
        Optional<Account> accountOptional = accountRepository.findById(id);
        if (accountOptional.isEmpty()) {
            throw new ResourceNotFoundException(id);
        }

        Account account = accountOptional.get();
        if (account.getBalance() > 0) {
            throw new ActiveBalanceException(id, account.getBalance());
        }
        accountRepository.deleteById(id);
    }


}
