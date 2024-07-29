package com.example.foneproject.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AccountCustomerReqDTO {
    @NotNull(message = "Please associate an existing user email with your account")
    private String email;
}