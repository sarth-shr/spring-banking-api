package com.example.foneproject.controller;

import com.example.foneproject.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/transactions")
public class TransactionController {
    private final TransactionService transactionService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getTransactions(@RequestParam(name = "page", defaultValue = "0") int page) {
        return transactionService.getAll(page);
    }

    @GetMapping("/get")
    public ResponseEntity<Map<String, Object>> getTransactionByAccount(@RequestParam("accId") int accId, @RequestParam(name = "page", defaultValue = "0") int page) {
        return transactionService.getByAccount(page, accId);
    }
}
