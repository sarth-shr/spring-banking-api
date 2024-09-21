package com.example.foneproject.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AccTransferReqDTO {
    @NotBlank(message = "Required Field")
    private String fromAccNumber;

    @NotBlank(message = "Required Field")
    private String toAccNumber;

    @NotNull(message = "Required Field")
    @Min(value = 1000, message = "Minimum amount is 1000")
    private int amount;
}
