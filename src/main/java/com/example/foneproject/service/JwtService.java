package com.example.foneproject.service;

import com.example.foneproject.dto.request.JwtAuthReqDTO;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface JwtService {
    ResponseEntity<Map<String, Object>> get(JwtAuthReqDTO jwtAuthReqDTO);
}
