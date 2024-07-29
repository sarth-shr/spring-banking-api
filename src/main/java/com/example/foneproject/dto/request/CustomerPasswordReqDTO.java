package com.example.foneproject.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CustomerPasswordReqDTO {
    @NotBlank(message = "Password cannot be empty")
    private String currentPassword;

    @NotBlank(message = "New Password cannot be empty")
    @Size(min = 8, message = "Minimum of 8 characters")
    private String password;

}
