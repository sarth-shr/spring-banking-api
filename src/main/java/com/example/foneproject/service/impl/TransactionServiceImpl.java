package com.example.foneproject.service.impl;

import com.example.foneproject.dto.response.TransactionResDTO;
import com.example.foneproject.entity.Account;
import com.example.foneproject.entity.Transaction;
import com.example.foneproject.exception.ResourceNotFoundException;
import com.example.foneproject.handler.ErrorResponseHandler;
import com.example.foneproject.handler.OkResponseHandler;
import com.example.foneproject.repository.TransactionRepository;
import com.example.foneproject.service.TransactionService;
import com.example.foneproject.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final ModelMapper modelMapper;
    private final OkResponseHandler okResponseHandler;
    private final ErrorResponseHandler errorResponseHandler;
    private final TransactionRepository transactionRepository;

    public void saveDeposit(Account account, int amount) {
        try {
            Transaction transaction = Transaction.builder()
                    .type("DEPOSIT")
                    .transactionNumber(generateTransactionNumber())
                    .toAccount(account)
                    .toAccOldBalance(account.getBalance())
                    .toAccNewBalance(account.getBalance() + amount)
                    .amount(amount)
                    .date(new Date())
                    .build();

            transactionRepository.save(transaction);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public void saveTransfer(Account fromAccount, Account toAccount, int amount) {
        try {
            Transaction transaction = Transaction.builder()
                    .type("TRANSFER")
                    .transactionNumber(generateTransactionNumber())
                    .fromAccount(fromAccount)
                    .fromAccOldBalance(fromAccount.getBalance())
                    .fromAccNewBalance(fromAccount.getBalance() - amount)
                    .toAccount(toAccount)
                    .toAccOldBalance(toAccount.getBalance())
                    .toAccNewBalance(toAccount.getBalance() + amount)
                    .toAccount(toAccount)
                    .amount(amount)
                    .date(new Date())
                    .build();

            transactionRepository.save(transaction);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public void saveInterest(Account account, int amount) {
        try {
            Transaction transaction = Transaction.builder()
                    .type("INTEREST CREDITED")
                    .transactionNumber(generateTransactionNumber())
                    .toAccount(account)
                    .toAccOldBalance(account.getBalance() - amount)
                    .toAccNewBalance(account.getBalance())
                    .amount(amount)
                    .date(new Date())
                    .build();

            transactionRepository.save(transaction);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<ApiResponse> getAll(int page) {
        try {
            Pageable pageable = PageRequest.of(page, 3);

            Page<Transaction> transactionPage = transactionRepository.findAll(pageable);
            Page<TransactionResDTO> transactionDTOPage = transactionPage.map(transaction -> modelMapper.map(transaction, TransactionResDTO.class));

            return okResponseHandler.getPaginated("Retrieved all transactions", HttpStatus.OK, transactionDTOPage);
        } catch (Exception e) {
            return errorResponseHandler.get(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    @Override
    public ResponseEntity<ApiResponse> getAllByAccount(String accNumber, int page) {
        try {
            if (transactionRepository.findByFromAccount_AccNumberOrToAccount_AccNumber(accNumber, accNumber).isEmpty()) {
                throw new ResourceNotFoundException(accNumber);
            }
            Pageable pageRequest = PageRequest.of(page, 3);

            Page<Transaction> transactionPage = transactionRepository.findByFromAccount_AccNumberOrToAccount_AccNumber(accNumber, accNumber, pageRequest);
            Page<TransactionResDTO> transactionDTOPage = transactionPage.map(transaction -> modelMapper.map(transaction, TransactionResDTO.class));

            return okResponseHandler.getPaginated("Retrieved all transactions associated with A/C #" + accNumber, HttpStatus.OK, transactionDTOPage);
        } catch (Exception e) {
            return errorResponseHandler.get(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    private String generateTransactionNumber() {
        String transactionNumber;
        do {
            transactionNumber = UUID.randomUUID().toString().substring(0, 13).replaceAll("-", "").toUpperCase();
        } while (transactionRepository.findByTransactionNumber(transactionNumber).isPresent());
        return transactionNumber;
    }
}
