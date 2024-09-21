package com.example.foneproject.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Date;

@Getter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class ApiResponse {
    private final Date timestamp;
    private final int code;
    private final HttpStatus status;
    private final String message;
    private final Object response;

    public ApiResponse(String message, HttpStatus status) {
        this(new Date(), status.value(), status, message, null);
    }

    public ApiResponse(Object response, HttpStatus status) {
        this(new Date(), status.value(), status, null, response);
    }

    public ApiResponse(String message, HttpStatus status, Object response) {
        this(new Date(), status.value(), status, message, response);
    }
}
