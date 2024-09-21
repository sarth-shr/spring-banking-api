package com.example.foneproject.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AccResDTO {
    private String accountNumber;
    private String type;
    private int balance;
    private float interest;
    private String customerEmail;
}
