package com.example.foneproject.controller;

import com.example.foneproject.service.TransactionService;
import com.example.foneproject.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/transactions")
public class TransactionController {
    private final TransactionService transactionService;

    @GetMapping
    public ResponseEntity<ApiResponse> getTransactions(@RequestParam(name = "page", defaultValue = "0") int page) {
        return transactionService.getAll(page);
    }

    @GetMapping("/get")
    public ResponseEntity<ApiResponse> getTransactionByAccount(@RequestParam("accNumber") String accNumber, @RequestParam(name = "page", defaultValue = "0") int page) {
        return transactionService.getAllByAccount(accNumber, page);
    }
}
