package com.example.foneproject.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class JwtAuthReqDTO {
    @NotEmpty(message = "Email cannot be empty")
    private String email;

    @NotEmpty(message = "Email cannot be empty")
    private String password;
}
