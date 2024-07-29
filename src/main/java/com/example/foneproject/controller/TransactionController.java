package com.example.foneproject.controller;

import com.example.foneproject.dto.response.TransactionResDTO;
import com.example.foneproject.entity.Transaction;
import com.example.foneproject.handler.JsonResponseHandler;
import com.example.foneproject.handler.PaginationResponseHandler;
import com.example.foneproject.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/transactions")
public class TransactionController {
    private final ModelMapper modelMapper;
    private final TransactionService transactionService;
    private final JsonResponseHandler jsonResponseHandler;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getTransactions(@RequestParam(name = "page", defaultValue = "0") int page) {
        Page<Transaction> transactions = transactionService.getAll(page);
        Page<TransactionResDTO> transactionResDTOPage = transactions.map(transaction -> modelMapper.map(transaction, TransactionResDTO.class));
        PaginationResponseHandler<TransactionResDTO> transactionPage = new PaginationResponseHandler<>(transactionResDTOPage);

        return jsonResponseHandler.get("Retrieved all transactions", HttpStatus.OK.value(), HttpStatus.OK, transactionPage);
    }

    @GetMapping("/{accId}")
    public ResponseEntity<Map<String, Object>> getTransactionByAccount(@PathVariable("accId") int accId, @RequestParam(name = "page", defaultValue = "0") int page) {
        Page<Transaction> transactions = transactionService.getByAccount(page, accId);
        Page<TransactionResDTO> transactionResDTOPage = transactions.map(transaction -> modelMapper.map(transaction, TransactionResDTO.class));
        PaginationResponseHandler<TransactionResDTO> transactionPage = new PaginationResponseHandler<>(transactionResDTOPage);

        return jsonResponseHandler.get("Retrieved all transactions associated with account ID: " + accId, HttpStatus.OK.value(), HttpStatus.OK, transactionPage);
    }
}
