package com.example.foneproject.service;

import com.example.foneproject.dto.request.AuthoritiesReqDTO;
import com.example.foneproject.entity.Customer;
import com.example.foneproject.util.ApiResponse;
import org.springframework.http.ResponseEntity;

public interface UserCredentialsService {
    void loadAdmin();

    void save(Customer customer);

    void updateEmail(String currentEmail, String updatedEmail);

    void updatePassword(String email, String updatedPassword);

    ResponseEntity<ApiResponse> disableUser(String email);

    ResponseEntity<ApiResponse> enableUser(String email);

    ResponseEntity<ApiResponse> assignRole(String email, AuthoritiesReqDTO authoritiesReqDTO);
}
