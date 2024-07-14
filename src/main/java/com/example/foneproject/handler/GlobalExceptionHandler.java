package com.example.foneproject.handler;

import com.example.foneproject.exception.*;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    @ExceptionHandler({ObjectNotFoundException.class, EmptyListException.class})
    public ResponseEntity<Map<String, Object>> handleNotFoundException(RuntimeException e) {
        Map<String, Object> responseMap = new LinkedHashMap<>();
        responseMap.put("timestamp", new Date());
        responseMap.put("code", HttpStatus.NOT_FOUND.value());
        responseMap.put("status", HttpStatus.NOT_FOUND);

        Map<String, Object> errorMap = new LinkedHashMap<>();
        errorMap.put("message", e.getMessage());

        responseMap.put("errors", errorMap);
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
        Map<String, Object> responseMap = new LinkedHashMap<>();
        responseMap.put("timestamp", new Date());
        responseMap.put("code", HttpStatus.BAD_REQUEST.value());
        responseMap.put("status", HttpStatus.BAD_REQUEST);

        Map<String, Object> errorMap = new LinkedHashMap<>();
        errorMap.put("message", e.getMessage());

        responseMap.put("errors", errorMap);
        return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({BadCredentialsException.class, UsernameNotFoundException.class})
    public ResponseEntity<Map<String, Object>> handleUnauthorizedException(RuntimeException e) {
        Map<String, Object> responseMap = new LinkedHashMap<>();
        responseMap.put("timestamp", new Date());
        responseMap.put("code", HttpStatus.UNAUTHORIZED.value());
        responseMap.put("status", HttpStatus.UNAUTHORIZED);

        Map<String, Object> errorMap = new LinkedHashMap<>();
        errorMap.put("message", e.getMessage());

        responseMap.put("errors", errorMap);
        return new ResponseEntity<>(responseMap, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({SignatureException.class, ExpiredJwtException.class, AccessDeniedException.class})
    public ResponseEntity<Map<String, Object>> handleForbiddenException(RuntimeException e) {
        Map<String, Object> responseMap = new LinkedHashMap<>();
        responseMap.put("timestamp", new Date());
        responseMap.put("code", HttpStatus.FORBIDDEN.value());
        responseMap.put("status", HttpStatus.FORBIDDEN);

        Map<String, Object> errorMap = new LinkedHashMap<>();
        errorMap.put("message", e.getMessage());

        responseMap.put("errors", errorMap);
        return new ResponseEntity<>(responseMap, HttpStatus.FORBIDDEN);
    }
}