package com.example.foneproject.handler;

import com.example.foneproject.exception.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
            InsufficientFundsException.class,
            InvalidCredentialsException.class,
            InvalidEmailException.class,
            InvalidTransferIdException.class,
            SameTransferIdException.class,
            UnsupportedAccTypeException.class,
            ActiveBalanceException.class
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
}
