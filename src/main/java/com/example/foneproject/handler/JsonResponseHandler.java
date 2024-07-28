package com.example.foneproject.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JsonResponseHandler {
    public ResponseEntity<Map<String, Object>> get(String message, int statusCode, HttpStatus httpStatus, Object response) {
        Map<String, Object> responseMap = new LinkedHashMap<>();
        responseMap.put("timestamp", new Date());
        responseMap.put("code", statusCode);
        responseMap.put("status", httpStatus);
        responseMap.put("message", message);
        responseMap.put("data", response);

        return new ResponseEntity<>(responseMap, httpStatus);
    }

    public ResponseEntity<Map<String, Object>> get(String message, int statusCode, HttpStatus httpStatus) {
        Map<String, Object> responseMap = new LinkedHashMap<>();
        responseMap.put("timestamp", new Date());
        responseMap.put("code", statusCode);
        responseMap.put("status", httpStatus);
        responseMap.put("message", message);

        return new ResponseEntity<>(responseMap, httpStatus);
    }

    public ResponseEntity<Map<String, Object>> get(String message, int statusCode, HttpStatus httpStatus, String token) {
        Map<String, Object> responseMap = new LinkedHashMap<>();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", token);
        headers.add("Access-Control-Expose-Headers", "Authorization");
        responseMap.put("timestamp", new Date());
        responseMap.put("code", statusCode);
        responseMap.put("status", httpStatus);
        responseMap.put("message", message);

        return new ResponseEntity<>(responseMap, headers, httpStatus);
    }
}
