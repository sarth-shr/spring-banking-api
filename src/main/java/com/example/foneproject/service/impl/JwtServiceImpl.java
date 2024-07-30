package com.example.foneproject.service.impl;

import com.example.foneproject.dto.request.JwtAuthReqDTO;
import com.example.foneproject.handler.JsonResponseHandler;
import com.example.foneproject.service.JwtService;
import com.example.foneproject.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {
    private final JwtUtils jwtUtils;
    private final JsonResponseHandler jsonResponseHandler;
    private final AuthenticationManager authenticationManager;

    @Override
    public ResponseEntity<Map<String, Object>> get(JwtAuthReqDTO jwtAuthReqDTO) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(jwtAuthReqDTO.getEmail(), jwtAuthReqDTO.getPassword()));
        if (authentication.isAuthenticated()) {
            String token = jwtUtils.generateToken(jwtAuthReqDTO.getEmail());
            return jsonResponseHandler.get("JWT Generated", HttpStatus.OK.value(), HttpStatus.OK, token);
        }
        throw new BadCredentialsException("Invalid Credentials");
    }
}
