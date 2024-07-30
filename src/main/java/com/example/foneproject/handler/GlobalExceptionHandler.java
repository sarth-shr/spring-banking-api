package com.example.foneproject.handler;

import com.example.foneproject.exception.*;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFoundException(RuntimeException e) {
        Map<String, Object> responseMap = new LinkedHashMap<>();
        responseMap.put("timestamp", new Date());
        responseMap.put("code", HttpStatus.NOT_FOUND.value());
        responseMap.put("status", HttpStatus.NOT_FOUND);
        responseMap.put("error", e.getMessage());

        return new ResponseEntity<>(responseMap, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({
            InvalidEmailException.class,
            ActiveBalanceException.class,
            SameTransferIdException.class,
            InsufficientFundsException.class,
            InvalidTransferIdException.class,
            InvalidCredentialsException.class,
            UnsupportedAccTypeException.class,
    })
    public ResponseEntity<Map<String, Object>> handleBadRequestException(RuntimeException e) {
        Map<String, Object> errorMap = new LinkedHashMap<>();
        errorMap.put("timestamp", new Date());
        errorMap.put("code", HttpStatus.BAD_REQUEST.value());
        errorMap.put("status", HttpStatus.BAD_REQUEST);
        errorMap.put("error", e.getMessage());

        return new ResponseEntity<>(errorMap, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({BadCredentialsException.class, UsernameNotFoundException.class})
    public ResponseEntity<Map<String, Object>> handleUnauthorizedException(RuntimeException e) {
        Map<String, Object> errorMap = new LinkedHashMap<>();
        errorMap.put("timestamp", new Date());
        errorMap.put("code", HttpStatus.UNAUTHORIZED.value());
        errorMap.put("status", HttpStatus.UNAUTHORIZED);
        errorMap.put("error", e.getMessage());

        return new ResponseEntity<>(errorMap, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({SignatureException.class, ExpiredJwtException.class, AccessDeniedException.class})
    public ResponseEntity<Map<String, Object>> handleForbiddenException(RuntimeException e) {
        Map<String, Object> errorMap = new LinkedHashMap<>();
        errorMap.put("timestamp", new Date());
        errorMap.put("code", HttpStatus.FORBIDDEN.value());
        errorMap.put("status", HttpStatus.FORBIDDEN);
        errorMap.put("error", e.getMessage());

        return new ResponseEntity<>(errorMap, HttpStatus.FORBIDDEN);
    }
}