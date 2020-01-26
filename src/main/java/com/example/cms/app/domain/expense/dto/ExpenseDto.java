package com.example.cms.app.domain.expense.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExpenseDto {

    private Long id;
    private String title;
    private BigDecimal amount;
    private LocalDateTime createdAt;
    private String createdBy;

}
