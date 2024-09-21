package com.example.foneproject.handler;

import com.example.foneproject.util.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class ErrorResponseHandler {
    public ResponseEntity<ApiResponse> get(String message, HttpStatus httpStatus) {
        ApiResponse error = new ApiResponse(message, httpStatus);
        return new ResponseEntity<>(error, httpStatus);
    }
}
