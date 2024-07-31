package com.example.foneproject.service.impl;

import com.example.foneproject.dto.request.AccountReqDTO;
import com.example.foneproject.dto.response.AccountResDTO;
import com.example.foneproject.entity.Account;
import com.example.foneproject.entity.Customer;
import com.example.foneproject.exception.*;
import com.example.foneproject.handler.ErrorResponseHandler;
import com.example.foneproject.handler.OkResponseHandler;
import com.example.foneproject.handler.PaginationResponseHandler;
import com.example.foneproject.repository.AccountRepository;
import com.example.foneproject.repository.CustomerRepository;
import com.example.foneproject.service.AccountService;
import com.example.foneproject.service.TransactionService;
import com.example.foneproject.util.EmailUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final EmailUtils emailUtils;
    private final ModelMapper modelMapper;
    private final OkResponseHandler okResponseHandler;
    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;
    private final TransactionService transactionService;
    private final ErrorResponseHandler errorResponseHandler;

    @Override
    public ResponseEntity<Map<String, Object>> get(int id) {
        try {
            Optional<Account> accountOptional = accountRepository.findById(id);
            if (accountOptional.isEmpty()) {
                throw new ResourceNotFoundException(id);
            }

            Account account = accountOptional.get();
            AccountResDTO accountResDTO = modelMapper.map(account, AccountResDTO.class);

            return okResponseHandler.get("Retrieved account with ID: " + id, HttpStatus.OK, accountResDTO);
        } catch (Exception e) {
            return errorResponseHandler.get(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<Map<String, Object>> getAllByEmail(String email, int page) {
        try {
            Pageable pageRequest = PageRequest.of(page, 3);

            Page<Account> accountPage = accountRepository.findByEmailPageable(pageRequest, email);
            Page<AccountResDTO> accountDTOPage = accountPage.map(account -> modelMapper.map(account, AccountResDTO.class));
            PaginationResponseHandler<AccountResDTO> accounts = new PaginationResponseHandler<>(accountDTOPage);

            return okResponseHandler.get("Retrieved accounts associated with email: " + email, HttpStatus.OK, accounts);
        } catch (Exception e) {
            return errorResponseHandler.get(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<Map<String, Object>> getAll(int page) {
        try {
            Pageable pageRequest = PageRequest.of(page, 3);

            Page<Account> accountPage = accountRepository.findAll(pageRequest);
            Page<AccountResDTO> accountDTOPage = accountPage.map(account -> modelMapper.map(account, AccountResDTO.class));
            PaginationResponseHandler<AccountResDTO> accounts = new PaginationResponseHandler<>(accountDTOPage);

            return okResponseHandler.get("Retrieved all accounts", HttpStatus.OK, accounts);
        } catch (Exception e) {
            return errorResponseHandler.get(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<Map<String, Object>> open(AccountReqDTO accountReqDTO) {
        try {
            Account account = modelMapper.map(accountReqDTO, Account.class);

            String email = account.getCustomer().getEmail();
            Optional<Customer> customerOptional = customerRepository.findByEmail(email);
            if (customerOptional.isEmpty()) {
                throw new ResourceNotFoundException(email);
            }
            Customer registeredCustomer = customerOptional.get();
            int initialDeposit = registeredCustomer.getInitialDeposit();
            int openingBalance = account.getBalance();

            String accType = account.getType();
            if (!(accType.equalsIgnoreCase("saving") || accType.equals("checking"))) {
                throw new UnsupportedAccTypeException();
            }

            if (accType.equals("saving")) {
                account.setInterest(5.5f);
            } else {
                account.setInterest(0.0f);
            }

            List<Account> assoicatedEmailsList = accountRepository.findByCustomer_Email(registeredCustomer.getEmail());
            if (assoicatedEmailsList.isEmpty()) {
                account.setBalance(initialDeposit + openingBalance);
                accountRepository.save(account);
            } else {
                account.setBalance(openingBalance);
                accountRepository.save(account);
            }
            return okResponseHandler.get("Account successfully opened", HttpStatus.CREATED);
        } catch (Exception e) {
            return errorResponseHandler.get(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    @Transactional
    public ResponseEntity<Map<String, Object>> deposit(int accId, int amount) {
        try {
            Optional<Account> accountOptional = accountRepository.findById(accId);
            if (accountOptional.isEmpty()) {
                throw new ResourceNotFoundException(accId);
            }

            Account account = accountOptional.get();
            accountRepository.depositAmount(accId, amount);
            transactionService.saveDeposit(account, amount);

            emailUtils.sendMail(
                    account.getCustomer().getEmail(),
                    "Amount Deposited",
                    "Amount of " + amount + " has been deposited into your account with ID: " + accId
            );

            return okResponseHandler.get("Amount: " + amount + " deposited to account with ID: " + accId, HttpStatus.OK);
        } catch (Exception e) {
            return errorResponseHandler.get(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<Map<String, Object>> transfer(int fromId, int toId, int amount) {
        try {
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

            emailUtils.sendMail(
                    fromAccount.getCustomer().getEmail(),
                    "Balance Transferred",
                    "Amount of " + amount + " has been transferred from your account with ID: " + fromId + " into account with ID: " + toId
            );

            emailUtils.sendMail(
                    toAccount.getCustomer().getEmail(),
                    "Balance Transfer Received",
                    "Amount of " + amount + " has been transferred into your account with ID: " + toId + " from account with ID: " + fromId
            );

            return okResponseHandler.get("Amount: " + amount + " transferred from account with ID: " + fromId + " to account with ID: " + toId, HttpStatus.OK);
        } catch (Exception e) {
            return errorResponseHandler.get(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<Map<String, Object>> delete(int id) {
        try {
            Optional<Account> accountOptional = accountRepository.findById(id);
            if (accountOptional.isEmpty()) {
                throw new ResourceNotFoundException(id);
            }

            Account account = accountOptional.get();
            if (account.getBalance() > 0) {
                throw new ActiveBalanceException(id, account.getBalance());
            }
            accountRepository.deleteById(id);
            return okResponseHandler.get("Account ID: " + id + " deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            return errorResponseHandler.get(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
