package com.example.cms.security.domain.travelgroup.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateExpenseParticipants {

    private Long authorId;
    private Long groupId;
    private String title;
    private BigDecimal totalAmount;
    private List<ExpenseParticipantDto> debtors;
}
