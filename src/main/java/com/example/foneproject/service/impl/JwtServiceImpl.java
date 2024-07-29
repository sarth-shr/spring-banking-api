package com.example.foneproject.service.impl;

import com.example.foneproject.dto.request.JwtAuthReqDTO;
import com.example.foneproject.service.JwtService;
import com.example.foneproject.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    @Override
    public String get(JwtAuthReqDTO jwtAuthReqDTO) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(jwtAuthReqDTO.getEmail(), jwtAuthReqDTO.getPassword()));
        if (authentication.isAuthenticated()) {
            return jwtUtils.generateToken(jwtAuthReqDTO.getEmail());
        }
        throw new BadCredentialsException("Invalid Credentials");
    }
}
