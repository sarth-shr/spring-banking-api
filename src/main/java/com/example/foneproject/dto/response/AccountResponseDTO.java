package com.example.foneproject.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AccountResponseDTO {
    private int id;
    private String type;
    private int balance;
    private float interest;
    private String customerEmail;
}
