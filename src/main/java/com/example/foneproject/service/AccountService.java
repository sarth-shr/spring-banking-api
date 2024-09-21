package com.example.foneproject.service;

import com.example.foneproject.dto.request.AccDepositReqDTO;
import com.example.foneproject.dto.request.AccReqDTO;
import com.example.foneproject.dto.request.AccTransferReqDTO;
import com.example.foneproject.util.ApiResponse;
import org.springframework.http.ResponseEntity;

public interface AccountService {
    ResponseEntity<ApiResponse> get(String accNumber);

    ResponseEntity<ApiResponse> getAllByEmail(String email, int page);

    ResponseEntity<ApiResponse> getAll(int page);

    ResponseEntity<ApiResponse> open(AccReqDTO accReqDTO);

    ResponseEntity<ApiResponse> deposit(AccDepositReqDTO accDepositReqDTO);

    ResponseEntity<ApiResponse> transfer(AccTransferReqDTO accTransferReqDTO);

    ResponseEntity<ApiResponse> delete(String accNumber);
}
