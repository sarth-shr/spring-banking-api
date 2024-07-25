package com.example.foneproject.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AccountRequestDTO {
    @NotBlank(message = "Account type cannot be empty")
    private String type;

    @NotNull(message = "Please enter a valid balance")
    @Min(value = 5000, message = "Minimum opening balance must be 5000")
    private Integer balance;

    @Valid
    @NotNull(message = "Required field")
    private AccountCustomerRequestDTO customer;

}
