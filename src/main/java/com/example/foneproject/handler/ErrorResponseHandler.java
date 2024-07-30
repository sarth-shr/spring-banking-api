package com.example.foneproject.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class ErrorResponseHandler {
    public ResponseEntity<Map<String, Object>> get(String message, HttpStatus httpStatus) {
        Map<String, Object> errorMap = new LinkedHashMap<>();
        errorMap.put("timestamp", new Date());
        errorMap.put("code", httpStatus.value());
        errorMap.put("status", httpStatus);
        errorMap.put("error", message);

        return new ResponseEntity<>(errorMap, httpStatus);
    }
}
