package com.example.foneproject.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class TransactionResDTO {
    private Date date;
    private String type;
    private String transactionNumber;
    private int amount;
    private String fromAccNumber;
    private int fromAccOldBalance;
    private int fromAccNewBalance;
    private String toAccNumber;
    private int toAccOldBalance;
    private int toAccNewBalance;
}

