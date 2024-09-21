package com.example.foneproject.service.impl;

import com.example.foneproject.dto.request.AccDepositReqDTO;
import com.example.foneproject.dto.request.AccReqDTO;
import com.example.foneproject.dto.request.AccTransferReqDTO;
import com.example.foneproject.dto.response.AccResDTO;
import com.example.foneproject.entity.Account;
import com.example.foneproject.entity.Customer;
import com.example.foneproject.exception.*;
import com.example.foneproject.handler.ErrorResponseHandler;
import com.example.foneproject.handler.OkResponseHandler;
import com.example.foneproject.repository.AccountRepository;
import com.example.foneproject.repository.CustomerRepository;
import com.example.foneproject.service.AccountService;
import com.example.foneproject.service.TransactionService;
import com.example.foneproject.util.ApiResponse;
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
import java.util.Optional;
import java.util.UUID;

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
    public ResponseEntity<ApiResponse> get(String accNumber) {
        try {
            Optional<Account> accountOptional = accountRepository.findByAccNumber(accNumber);
            if (accountOptional.isEmpty()) {
                throw new ResourceNotFoundException(accNumber);
            }

            Account account = accountOptional.get();
            AccResDTO accResDTO = modelMapper.map(account, AccResDTO.class);

            return okResponseHandler.getContent("Retrieved A/C #" + accNumber, HttpStatus.OK, accResDTO);
        } catch (Exception e) {
            return errorResponseHandler.get(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<ApiResponse> getAllByEmail(String email, int page) {
        try {
            Pageable pageRequest = PageRequest.of(page, 3);

            Page<Account> accountPage = accountRepository.findByEmailPageable(email, pageRequest);
            Page<AccResDTO> accountDTOPage = accountPage.map(account -> modelMapper.map(account, AccResDTO.class));

            return okResponseHandler.getPaginated("Retrieved accounts associated with email: " + email, HttpStatus.OK, accountDTOPage);
        } catch (Exception e) {
            return errorResponseHandler.get(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<ApiResponse> getAll(int page) {
        try {
            Pageable pageRequest = PageRequest.of(page, 3);

            Page<Account> accountPage = accountRepository.findAll(pageRequest);
            Page<AccResDTO> accountDTOPage = accountPage.map(account -> modelMapper.map(account, AccResDTO.class));

            return okResponseHandler.getPaginated("Retrieved all accounts", HttpStatus.OK, accountDTOPage);
        } catch (Exception e) {
            return errorResponseHandler.get(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<ApiResponse> open(AccReqDTO accReqDTO) {
        try {
            Account account = modelMapper.map(accReqDTO, Account.class);
            String customerEmail = account.getCustomer().getEmail();

            Optional<Customer> customerOptional = customerRepository.findByEmail(customerEmail);
            if (customerOptional.isEmpty()) {
                throw new ResourceNotFoundException(customerEmail);
            }

            Customer registeredCustomer = customerOptional.get();
            int initialDeposit = registeredCustomer.getInitialDeposit();
            int openingBalance = account.getBalance();

            String accType = account.getType();
            if (!(accType.equalsIgnoreCase("savings") || accType.equalsIgnoreCase("checking"))) {
                throw new UnsupportedAccTypeException();
            }

            if (accType.equals("savings")) {
                account.setInterest(5.5f);
            } else {
                account.setInterest(0.0f);
            }
            account.setAccNumber(genAccountNumber());
            account.setCustomer(registeredCustomer);

            List<Account> associatedEmailsList = accountRepository.findByCustomer_Email(registeredCustomer.getEmail());
            if (associatedEmailsList.isEmpty()) {
                account.setBalance(initialDeposit + openingBalance);
                accountRepository.save(account);
            } else {
                account.setBalance(openingBalance);
                for (Account acc : associatedEmailsList) {
                    if (acc.getType().equals(accReqDTO.getType())) {
                        throw new DuplicateAccTypeException(accReqDTO.getType());
                    }
                }
                accountRepository.save(account);
            }

            return okResponseHandler.get("Account successfully opened", HttpStatus.CREATED);
        } catch (Exception e) {
            return errorResponseHandler.get(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    @Transactional
    public ResponseEntity<ApiResponse> deposit(AccDepositReqDTO accDepositReqDTO) {
        try {
            String accNumber = accDepositReqDTO.getAccNumber();
            int amount = accDepositReqDTO.getAmount();

            Optional<Account> accountOptional = accountRepository.findByAccNumber(accNumber);
            if (accountOptional.isEmpty()) {
                throw new ResourceNotFoundException(accNumber);
            }

            Account account = accountOptional.get();
            accountRepository.increaseFunds(accNumber, amount);
            transactionService.saveDeposit(account, amount);

//            emailUtils.sendMail(
//                    account.getCustomer().getEmail(),
//                    "Amount Deposited",
//                    "Amount of " + amount + " has been deposited into your account with ID: " + accId
//            );

            return okResponseHandler.get("Amount: " + amount + " deposited to A/C #" + accNumber, HttpStatus.OK);
        } catch (Exception e) {
            return errorResponseHandler.get(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    @Transactional
    public ResponseEntity<ApiResponse> transfer(AccTransferReqDTO accTransferReqDTO) {
        try {
            String fromAccNumber = accTransferReqDTO.getFromAccNumber();
            String toAccNumber = accTransferReqDTO.getToAccNumber();
            int amount = accTransferReqDTO.getAmount();

            if (fromAccNumber.equals(toAccNumber)) {
                throw new SameTransferIdException();
            }

            Optional<Account> fromAccountOptional = accountRepository.findByAccNumber(fromAccNumber);
            Optional<Account> toAccountOptional = accountRepository.findByAccNumber(toAccNumber);
            if (fromAccountOptional.isEmpty() || toAccountOptional.isEmpty()) {
                throw new InvalidTransferIdException();
            }

            Account fromAccount = fromAccountOptional.get();
            Account toAccount = toAccountOptional.get();

            int fromAccountOldBalance = fromAccount.getBalance();
            if (fromAccountOldBalance < amount) {
                throw new InsufficientFundsException();
            }

            accountRepository.decreaseFunds(fromAccNumber, amount);
            accountRepository.increaseFunds(toAccNumber, amount);

            transactionService.saveTransfer(fromAccount, toAccount, amount);

//            emailUtils.sendMail(
//                    fromAccount.getCustomer().getEmail(),
//                    "Balance Transferred",
//                    "Amount of " + amount + " has been transferred from your account with ID: " + fromId + " into account with ID: " + toId
//            );
//
//            emailUtils.sendMail(
//                    toAccount.getCustomer().getEmail(),
//                    "Balance Transfer Received",
//                    "Amount of " + amount + " has been transferred into your account with ID: " + toId + " from account with ID: " + fromId
//            );

            return okResponseHandler.get("Amount: " + amount + " transferred from A/C #" + fromAccNumber + " to A/C #" + toAccNumber, HttpStatus.OK);
        } catch (Exception e) {
            return errorResponseHandler.get(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<ApiResponse> delete(String accNumber) {
        try {
            Optional<Account> accountOptional = accountRepository.findByAccNumber(accNumber);
            if (accountOptional.isEmpty()) {
                throw new ResourceNotFoundException(accNumber);
            }

            Account account = accountOptional.get();
            if (account.getBalance() > 0) {
                throw new ActiveBalanceException(accNumber, account.getBalance());
            }
            accountRepository.deleteById(account.getId());
            return okResponseHandler.get("A/C #" + accNumber + " deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            return errorResponseHandler.get(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    private String genAccountNumber() {
        String accNumber;
        do {
            accNumber = UUID.randomUUID().toString().substring(0, 13).replaceAll("-", "").toUpperCase();
        } while (accountRepository.findByAccNumber(accNumber).isPresent());
        return accNumber;
    }
}
