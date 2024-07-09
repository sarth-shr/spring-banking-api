package com.example.foneproject.service;

import com.example.foneproject.dto.request.JwtAuthRequestDTO;

public interface JwtService {
    String get(JwtAuthRequestDTO jwtAuthRequestDTO);
}
