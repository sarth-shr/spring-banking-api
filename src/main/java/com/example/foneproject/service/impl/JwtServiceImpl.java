package com.example.foneproject.service.impl;

import com.example.foneproject.dto.request.JwtAuthRequestDTO;
import com.example.foneproject.exception.InvalidCredentialsException;
import com.example.foneproject.service.JwtService;
import com.example.foneproject.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    @Override
    public String get(JwtAuthRequestDTO jwtAuthRequestDTO) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(jwtAuthRequestDTO.getEmail(), jwtAuthRequestDTO.getPassword()));
        if (authentication.isAuthenticated()) {
            return jwtUtils.generateToken(jwtAuthRequestDTO.getEmail());
        }
        throw new InvalidCredentialsException();
    }
}
