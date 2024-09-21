package com.example.foneproject.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AccDepositReqDTO {
    @NotBlank(message = "A/C # cannot be blank")
    private String accNumber;

    @NotNull(message = "Required Field")
    @Min(value = 1000, message = "Minimum amount is 1000")
    private int amount;
}
