package com.example.foneproject.handler;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class OkResponseHandler {
    public ResponseEntity<Map<String, Object>> get(String message, HttpStatus httpStatus) {
        Map<String, Object> responseMap = new LinkedHashMap<>();
        responseMap.put("timestamp", new Date());
        responseMap.put("code", httpStatus.value());
        responseMap.put("status", httpStatus);
        responseMap.put("message", message);

        return new ResponseEntity<>(responseMap, httpStatus);
    }

    public ResponseEntity<Map<String, Object>> getContent(String message, HttpStatus httpStatus, Object response) {
        Map<String, Object> responseMap = new LinkedHashMap<>();
        responseMap.put("timestamp", new Date());
        responseMap.put("code", httpStatus.value());
        responseMap.put("status", httpStatus);
        responseMap.put("message", message);
        responseMap.put("data", response);

        return new ResponseEntity<>(responseMap, httpStatus);
    }

    public ResponseEntity<Map<String, Object>> getHeaders(String message, HttpStatus httpStatus, String token) {
        Map<String, Object> responseMap = new LinkedHashMap<>();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", token);
        headers.add("Access-Control-Expose-Headers", "Authorization");
        responseMap.put("timestamp", new Date());
        responseMap.put("code", httpStatus.value());
        responseMap.put("status", httpStatus);
        responseMap.put("message", message);

        return new ResponseEntity<>(responseMap, headers, httpStatus);
    }

    public ResponseEntity<Map<String, Object>> getPaginated(String message, HttpStatus httpStatus, Page<?> paginatedResponse) {
        Map<String, Object> responseMap = new LinkedHashMap<>();
        HttpHeaders headers = new HttpHeaders();
        responseMap.put("timestamp", new Date());
        responseMap.put("code", httpStatus.value());
        responseMap.put("status", httpStatus);
        responseMap.put("message", message);

        Map<String, Object> pageableMap = new LinkedHashMap<>();
        pageableMap.put("content", paginatedResponse.getContent());
        pageableMap.put("totalItems", paginatedResponse.getTotalElements());
        pageableMap.put("totalPages", paginatedResponse.getTotalPages());
        pageableMap.put("pageSize", paginatedResponse.getSize());
        pageableMap.put("pageNumber", paginatedResponse.getNumber());

        responseMap.put("data", pageableMap);

        return new ResponseEntity<>(responseMap, headers, httpStatus);

    }
}
