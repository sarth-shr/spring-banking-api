package com.example.foneproject.controller;

import com.example.foneproject.dto.request.AccountReqDTO;
import com.example.foneproject.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/accounts")
public class AccountController {
    private final AccountService accountService;

    @GetMapping("/{accId}")
    public ResponseEntity<Map<String, Object>> getAccount(@PathVariable("accId") int id) {
        return accountService.get(id);
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAccounts(@RequestParam(name = "page", defaultValue = "0") int page) {
        return accountService.getAll(page);
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> openAccount(@Valid @RequestBody AccountReqDTO accountReqDTO) {
        return accountService.open(accountReqDTO);
    }

    @PostMapping("/deposit/{accId}")
    public ResponseEntity<Map<String, Object>> depositFunds(@PathVariable("accId") int accId, @RequestParam("amount") int amount) {
        return accountService.deposit(accId, amount);
    }

    @PostMapping("/transfer")
    public ResponseEntity<Map<String, Object>> transferFunds(@RequestParam("fromId") int fromId, @RequestParam("toId") int toId, @RequestParam("amount") int amount) {
        return accountService.transfer(fromId, toId, amount);
    }

    @DeleteMapping("delete/{accId}")
    public ResponseEntity<Map<String, Object>> deleteAccount(@PathVariable("accId") int id) {
        return accountService.delete(id);
    }
}
