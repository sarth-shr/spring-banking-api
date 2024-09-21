package com.example.foneproject.handler;

import com.example.foneproject.util.ApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class OkResponseHandler {
    public ResponseEntity<ApiResponse> get(String message, HttpStatus httpStatus) {
        ApiResponse success = new ApiResponse(message, httpStatus);
        return new ResponseEntity<>(success, httpStatus);
    }

    public ResponseEntity<ApiResponse> getContent(String message, HttpStatus httpStatus, Object response) {
        ApiResponse successWithData = new ApiResponse(message, httpStatus, response);
        return new ResponseEntity<>(successWithData, httpStatus);
    }

    public ResponseEntity<ApiResponse> getHeaders(String message, HttpStatus httpStatus, String token) {
        ApiResponse success = new ApiResponse(message, httpStatus);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", token);
        headers.add("Access-Control-Expose-Headers", "Authorization");

        return new ResponseEntity<>(success, headers, httpStatus);
    }

    public ResponseEntity<ApiResponse> getPaginated(String message, HttpStatus httpStatus, Page<?> paginatedResponse) {
        Map<String, Object> pageable = new LinkedHashMap<>();
        pageable.put("content", paginatedResponse.getContent());
        pageable.put("totalItems", paginatedResponse.getTotalElements());
        pageable.put("totalPages", paginatedResponse.getTotalPages());
        pageable.put("pageSize", paginatedResponse.getSize());
        pageable.put("pageNumber", paginatedResponse.getNumber());

        ApiResponse successWithData = new ApiResponse(message, httpStatus, pageable);
        return new ResponseEntity<>(successWithData, httpStatus);

    }
}
