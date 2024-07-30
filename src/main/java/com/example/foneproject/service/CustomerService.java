package com.example.foneproject.service;

import com.example.foneproject.dto.request.CustomerEmailReqDTO;
import com.example.foneproject.dto.request.CustomerInfoReqDTO;
import com.example.foneproject.dto.request.CustomerPasswordReqDTO;
import com.example.foneproject.dto.request.CustomerReqDTO;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface CustomerService {
    ResponseEntity<Map<String, Object>> save(CustomerReqDTO customerReqDTO);

    ResponseEntity<Map<String, Object>> get(String email);

    ResponseEntity<Map<String, Object>> getAll(int page);

    ResponseEntity<Map<String, Object>> updatePersonal(String email, CustomerInfoReqDTO customerInfoReqDTO);

    ResponseEntity<Map<String, Object>> updateEmail(String email, CustomerEmailReqDTO customerEmailReqDTO);

    ResponseEntity<Map<String, Object>> updatePassword(String email, CustomerPasswordReqDTO customerPasswordReqDTO);
}
