package com.example.foneproject.service;

import com.example.foneproject.dto.request.AuthoritiesReqDTO;
import com.example.foneproject.entity.Customer;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface UserCredentialsService {
    void loadAdmin();

    void save(Customer customer);

    void updateEmail(String email, Customer customer);

    void updatePassword(String email, Customer customer);

    ResponseEntity<Map<String, Object>> disableUser(String email);

    ResponseEntity<Map<String, Object>> enableUser(String email);

    ResponseEntity<Map<String, Object>> assignRole(String email, AuthoritiesReqDTO authoritiesReqDTO);
}
