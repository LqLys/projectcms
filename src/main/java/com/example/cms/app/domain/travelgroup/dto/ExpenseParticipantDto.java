package com.example.cms.app.domain.travelgroup.dto;

import lombok.*;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
public class ExpenseParticipantDto {
    @Getter
    @Setter
    private Long debtorId;
    @Getter
    private BigDecimal amount;

    public void setAmount(BigDecimal amount) {
        this.amount = amount == null ? BigDecimal.ZERO : amount;
    }
}
