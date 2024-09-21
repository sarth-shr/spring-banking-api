package com.example.foneproject.service;

import com.example.foneproject.dto.request.JwtAuthReqDTO;
import com.example.foneproject.util.ApiResponse;
import org.springframework.http.ResponseEntity;

public interface JwtService {
    ResponseEntity<ApiResponse> get(JwtAuthReqDTO jwtAuthReqDTO);
}
