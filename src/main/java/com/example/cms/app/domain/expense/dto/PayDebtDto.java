package com.example.cms.app.domain.expense.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PayDebtDto {

    private String lenderEmail;
    private BigDecimal amount;
}
