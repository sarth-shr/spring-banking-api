package com.example.foneproject.service;

import com.example.foneproject.dto.request.JwtAuthReqDTO;

public interface JwtService {
    String get(JwtAuthReqDTO jwtAuthReqDTO);
}
