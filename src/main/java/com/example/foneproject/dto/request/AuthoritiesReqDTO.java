package com.example.foneproject.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AuthoritiesReqDTO {
    @NotNull(message = "A user must be assigned authorities")
    private String authorities;
}
