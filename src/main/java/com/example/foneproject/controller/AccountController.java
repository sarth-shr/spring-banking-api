package com.example.foneproject.controller;

import com.example.foneproject.dto.request.AccDepositReqDTO;
import com.example.foneproject.dto.request.AccReqDTO;
import com.example.foneproject.dto.request.AccTransferReqDTO;
import com.example.foneproject.service.AccountService;
import com.example.foneproject.util.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/accounts")
public class AccountController {
    private final AccountService accountService;

    @GetMapping("/get-by-id")
    public ResponseEntity<ApiResponse> getAccount(@RequestParam("accNumber") String accNumber) {
        return accountService.get(accNumber);
    }

    @GetMapping("/get-by-email")
    public ResponseEntity<ApiResponse> getAccountsByEmail(@RequestParam("email") String email, @RequestParam(name = "page", defaultValue = "0") int page) {
        return accountService.getAllByEmail(email, page);
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAccounts(@RequestParam(name = "page", defaultValue = "0") int page) {
        return accountService.getAll(page);
    }

    @PostMapping
    public ResponseEntity<ApiResponse> openAccount(@Valid @RequestBody AccReqDTO accReqDTO) {
        return accountService.open(accReqDTO);
    }

    @PostMapping("/deposit")
    public ResponseEntity<ApiResponse> depositFunds(@Valid @RequestBody AccDepositReqDTO accDepositReqDTO) {
        return accountService.deposit(accDepositReqDTO);
    }

    @PostMapping("/transfer")
    public ResponseEntity<ApiResponse> transferFunds(@RequestBody @Valid AccTransferReqDTO accTransferReqDTO) {
        return accountService.transfer(accTransferReqDTO);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse> deleteAccount(@RequestParam("accNumber") String accNumber) {
        return accountService.delete(accNumber);
    }
}
