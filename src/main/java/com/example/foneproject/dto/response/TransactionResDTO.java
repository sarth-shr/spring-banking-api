package com.example.foneproject.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class TransactionResDTO {
    private int id;
    private Date date;
    private String type;
    private int amount;
    private int fromAccountId;
    private int fromAccOldBalance;
    private int fromAccNewBalance;
    private int toAccountId;
    private int toAccOldBalance;
    private int toAccNewBalance;
}

