package com.example.foneproject.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AccCustomerReqDTO {
    @NotNull(message = "Required Field")
    private String email;
}
