package com.example.foneproject.controller;

import com.example.foneproject.dto.request.AccountRequestDTO;
import com.example.foneproject.dto.response.AccountResponseDTO;
import com.example.foneproject.entity.Account;
import com.example.foneproject.handler.JsonResponseHandler;
import com.example.foneproject.handler.PaginationResponseHandler;
import com.example.foneproject.service.AccountService;
import com.example.foneproject.util.EmailUtils;
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
    private final EmailUtils emailUtils;
    private final ModelMapper modelMapper;
    private final AccountService accountService;
    private final JsonResponseHandler jsonResponseHandler;

    @GetMapping("/{accId}")
    public ResponseEntity<Map<String, Object>> getAccount(@PathVariable("accId") int id) {
        Account account = accountService.get(id);
        AccountResponseDTO accountResponseDTO = modelMapper.map(account, AccountResponseDTO.class);
        return jsonResponseHandler.get("Retrieved account with ID: " + id, HttpStatus.OK.value(), HttpStatus.OK, accountResponseDTO);
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAccounts(@RequestParam(name = "page", defaultValue = "0") int page) {
        Page<Account> accounts = accountService.getAll(page);
        Page<AccountResponseDTO> accountResponseDTOPage = accounts.map(account -> modelMapper.map(account, AccountResponseDTO.class));
        PaginationResponseHandler<AccountResponseDTO> accountPage = new PaginationResponseHandler<>(accountResponseDTOPage);

        return jsonResponseHandler.get("Retrieved all accounts", HttpStatus.OK.value(), HttpStatus.OK, accountPage);
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> openAccount(@Valid @RequestBody AccountRequestDTO accountRequestDTO) {
        Account account = modelMapper.map(accountRequestDTO, Account.class);
        accountService.open(account);

        return jsonResponseHandler.get("Account successfully opened", HttpStatus.CREATED.value(), HttpStatus.CREATED);
    }

    @PostMapping("/deposit/{accId}")
    public ResponseEntity<Map<String, Object>> depositFunds(@PathVariable("accId") int accId, @RequestParam("amount") int amount) {
        accountService.deposit(accId, amount);

        emailUtils.sendMail(
                accountService.get(accId).getCustomer().getEmail(),
                "Amount Deposited",
                "Amount of " + amount + " has been deposited into your account with ID: " + accId
        );

        return jsonResponseHandler.get("Amount: " + amount + " deposited to account with ID: " + accId, HttpStatus.OK.value(), HttpStatus.OK);
    }

    @PostMapping("/transfer")
    public ResponseEntity<Map<String, Object>> transferFunds(@RequestParam("fromId") int fromId, @RequestParam("toId") int toId, @RequestParam("amount") int amount) {
        accountService.transfer(fromId, toId, amount);

        emailUtils.sendMail(
                accountService.get(fromId).getCustomer().getEmail(),
                "Balance Transferred",
                "Amount of " + amount + " has been transferred from your account with ID: " + fromId + " into account with ID: " + toId
        );

        emailUtils.sendMail(
                accountService.get(toId).getCustomer().getEmail(),
                "Balance Transfer Received",
                "Amount of " + amount + " has been transferred into your account with ID: " + toId + " from account with ID: " + fromId
        );

        return jsonResponseHandler.get("Amount: " + amount + " transferred from account with ID: " + fromId + " to account with ID: " + toId, HttpStatus.OK.value(), HttpStatus.OK);
    }

    @DeleteMapping("delete/{accId}")
    public ResponseEntity<Map<String, Object>> deleteAccount(@PathVariable("accId") int id) {
        accountService.delete(id);

        return jsonResponseHandler.get("Account ID: " + id + " deleted successfully", HttpStatus.OK.value(), HttpStatus.OK);
    }
}
