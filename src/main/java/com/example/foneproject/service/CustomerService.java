package com.example.foneproject.service;

import com.example.foneproject.dto.request.CustomerEmailReqDTO;
import com.example.foneproject.dto.request.CustomerInfoReqDTO;
import com.example.foneproject.dto.request.CustomerPasswordReqDTO;
import com.example.foneproject.dto.request.CustomerReqDTO;
import com.example.foneproject.util.ApiResponse;
import org.springframework.http.ResponseEntity;

public interface CustomerService {

    ResponseEntity<ApiResponse> get(String email);

    ResponseEntity<ApiResponse> getAll(int page);

    ResponseEntity<ApiResponse> save(CustomerReqDTO customerReqDTO);

    ResponseEntity<ApiResponse> updatePersonal(String email, CustomerInfoReqDTO customerInfoReqDTO);

    ResponseEntity<ApiResponse> updateEmail(String email, CustomerEmailReqDTO customerEmailReqDTO);

    ResponseEntity<ApiResponse> updatePassword(String email, CustomerPasswordReqDTO customerPasswordReqDTO);
}
