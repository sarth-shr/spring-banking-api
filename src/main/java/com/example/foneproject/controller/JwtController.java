package com.example.foneproject.controller;

import com.example.foneproject.dto.request.JwtAuthRequestDTO;
import com.example.foneproject.handler.JsonResponseHandler;
import com.example.foneproject.service.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/jwt")
public class JwtController {
    private final JwtService jwtService;
    private final JsonResponseHandler jsonResponseHandler;

    @PostMapping
    public ResponseEntity<Map<String, Object>> getToken(@Valid @RequestBody JwtAuthRequestDTO jwtAuthRequestDTO) {
        String token = jwtService.get(jwtAuthRequestDTO);
        return jsonResponseHandler.get("Jwt Generated", HttpStatus.OK.value(), HttpStatus.OK, token);

    }
}
