package com.example.foneproject.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CustomerResponseDTO {
    private String firstName;
    private String lastName;
    private String email;
    private int initialDeposit;
}
