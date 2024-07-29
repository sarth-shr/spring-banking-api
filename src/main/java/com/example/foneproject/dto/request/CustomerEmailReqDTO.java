package com.example.foneproject.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CustomerEmailReqDTO {
    @Email(message = "Malformed email address")
    @NotBlank(message = "Email cannot be empty")
    private String updatedEmail;
}
