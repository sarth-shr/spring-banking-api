package com.example.foneproject.controller;

import com.example.foneproject.dto.request.JwtAuthReqDTO;
import com.example.foneproject.service.JwtService;
import com.example.foneproject.util.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/jwt")
public class JwtController {
    private final JwtService jwtService;

    @PostMapping
    public ResponseEntity<ApiResponse> getToken(@Valid @RequestBody JwtAuthReqDTO jwtAuthReqDTO) {
        return jwtService.get(jwtAuthReqDTO);
    }
}
