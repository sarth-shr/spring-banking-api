package com.example.foneproject.service;

import com.example.foneproject.dto.request.AccountReqDTO;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface AccountService {
    ResponseEntity<Map<String, Object>> get(int id);

    ResponseEntity<Map<String, Object>> getAllByEmail(String email, int page);

    ResponseEntity<Map<String, Object>> getAll(int page);

    ResponseEntity<Map<String, Object>> open(AccountReqDTO accountReqDTO);

    ResponseEntity<Map<String, Object>> deposit(int accId, int amount);

    ResponseEntity<Map<String, Object>> transfer(int fromId, int toId, int amount);

    ResponseEntity<Map<String, Object>> delete(int id);
}
