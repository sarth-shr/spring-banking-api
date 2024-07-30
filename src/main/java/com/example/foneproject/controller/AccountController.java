package com.example.foneproject.controller;

import com.example.foneproject.dto.request.AccountReqDTO;
import com.example.foneproject.dto.response.AccountResDTO;
import com.example.foneproject.entity.Account;
import com.example.foneproject.handler.JsonResponseHandler;
import com.example.foneproject.handler.PaginationResponseHandler;
import com.example.foneproject.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/accounts")
public class AccountController {
    private final ModelMapper modelMapper;
    private final AccountService accountService;
    private final JsonResponseHandler jsonResponseHandler;

    @GetMapping("/{accId}")
    public ResponseEntity<Map<String, Object>> getAccount(@PathVariable("accId") int id) {
        Account account = accountService.get(id);
        AccountResDTO accountResDTO = modelMapper.map(account, AccountResDTO.class);
        return jsonResponseHandler.get("Retrieved account with ID: " + id, HttpStatus.OK.value(), HttpStatus.OK, accountResDTO);
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAccounts(@RequestParam(name = "page", defaultValue = "0") int page) {
        Page<Account> accounts = accountService.getAll(page);
        Page<AccountResDTO> accountResDTOPage = accounts.map(account -> modelMapper.map(account, AccountResDTO.class));
        PaginationResponseHandler<AccountResDTO> accountPage = new PaginationResponseHandler<>(accountResDTOPage);

        return jsonResponseHandler.get("Retrieved all accounts", HttpStatus.OK.value(), HttpStatus.OK, accountPage);
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> openAccount(@Valid @RequestBody AccountReqDTO accountReqDTO) {
        Account account = modelMapper.map(accountReqDTO, Account.class);
        accountService.open(account);

        return jsonResponseHandler.get("Account successfully opened", HttpStatus.CREATED.value(), HttpStatus.CREATED);
    }

    @PostMapping("/deposit/{accId}")
    public ResponseEntity<Map<String, Object>> depositFunds(@PathVariable("accId") int accId, @RequestParam("amount") int amount) {
        accountService.deposit(accId, amount);

        return jsonResponseHandler.get("Amount: " + amount + " deposited to account with ID: " + accId, HttpStatus.OK.value(), HttpStatus.OK);
    }

    @PostMapping("/transfer")
    public ResponseEntity<Map<String, Object>> transferFunds(@RequestParam("fromId") int fromId, @RequestParam("toId") int toId, @RequestParam("amount") int amount) {
        accountService.transfer(fromId, toId, amount);

        return jsonResponseHandler.get("Amount: " + amount + " transferred from account with ID: " + fromId + " to account with ID: " + toId, HttpStatus.OK.value(), HttpStatus.OK);
    }

    @DeleteMapping("delete/{accId}")
    public ResponseEntity<Map<String, Object>> deleteAccount(@PathVariable("accId") int id) {
        accountService.delete(id);

        return jsonResponseHandler.get("Account ID: " + id + " deleted successfully", HttpStatus.OK.value(), HttpStatus.OK);
    }
}
