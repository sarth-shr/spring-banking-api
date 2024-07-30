package com.example.foneproject.service.impl;

import com.example.foneproject.dto.response.TransactionResDTO;
import com.example.foneproject.entity.Account;
import com.example.foneproject.entity.Transaction;
import com.example.foneproject.exception.ResourceNotFoundException;
import com.example.foneproject.handler.JsonResponseHandler;
import com.example.foneproject.handler.PaginationResponseHandler;
import com.example.foneproject.repository.TransactionRepository;
import com.example.foneproject.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final ModelMapper modelMapper;
    private final JsonResponseHandler jsonResponseHandler;
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
    public ResponseEntity<Map<String, Object>> getAll(int page) {
        Pageable pageable = PageRequest.of(page, 3);

        Page<Transaction> transactionPage = transactionRepository.findAll(pageable);
        Page<TransactionResDTO> transactionDTOPage = transactionPage.map(transaction -> modelMapper.map(transaction, TransactionResDTO.class));
        PaginationResponseHandler<TransactionResDTO> transactions = new PaginationResponseHandler<>(transactionDTOPage);

        return jsonResponseHandler.get("Retrieved all transactions", HttpStatus.OK.value(), HttpStatus.OK, transactions);

    }

    @Override
    public ResponseEntity<Map<String, Object>> getByAccount(int page, int accId) {
        List<Transaction> transactionAccount = transactionRepository.findByAccountId(accId);
        if (transactionAccount.isEmpty()) {
            throw new ResourceNotFoundException(accId);
        }

        Pageable pageable = PageRequest.of(page, 3);

        Page<Transaction> transactionPage = transactionRepository.findByAccountPageable(pageable, accId);
        Page<TransactionResDTO> transactionDTOPage = transactionPage.map(transaction -> modelMapper.map(transaction, TransactionResDTO.class));
        PaginationResponseHandler<TransactionResDTO> transactions = new PaginationResponseHandler<>(transactionDTOPage);

        return jsonResponseHandler.get("Retrieved all transactions associated with account ID: " + accId, HttpStatus.OK.value(), HttpStatus.OK, transactions);
    }
}
