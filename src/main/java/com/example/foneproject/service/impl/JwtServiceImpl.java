package com.example.foneproject.service.impl;

import com.example.foneproject.dto.request.JwtAuthReqDTO;
import com.example.foneproject.handler.ErrorResponseHandler;
import com.example.foneproject.handler.OkResponseHandler;
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
    private final OkResponseHandler okResponseHandler;
    private final ErrorResponseHandler errorResponseHandler;
    private final AuthenticationManager authenticationManager;

    @Override
    public ResponseEntity<Map<String, Object>> get(JwtAuthReqDTO jwtAuthReqDTO) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(jwtAuthReqDTO.getEmail(), jwtAuthReqDTO.getPassword()));
            if (authentication.isAuthenticated()) {
                String token = jwtUtils.generateToken(jwtAuthReqDTO.getEmail());
                return okResponseHandler.get("JWT Generated", HttpStatus.OK, token);
            }
            throw new BadCredentialsException("Invalid Credentials");
        } catch (RuntimeException e) {
            return errorResponseHandler.get(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }
}
