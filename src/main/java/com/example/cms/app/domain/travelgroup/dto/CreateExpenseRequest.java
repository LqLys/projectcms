package com.example.cms.app.domain.travelgroup.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateExpenseRequest {

    private Long authorId;
    private Long groupId;
    private String title;
    private BigDecimal amount;
    private List<Long> debtors;
}
