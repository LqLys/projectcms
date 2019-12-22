package com.example.cms.security.domain.expense.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UnpaidExpenseDto {

    private Long userId;
    private BigDecimal amount;
    private String relatedUser;
}
