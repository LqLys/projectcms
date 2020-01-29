package com.example.cms.app.domain.travelgroup.dto;

import com.example.cms.app.domain.expense.dto.ExpenseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TravelGroupExpensesDto {

    private Long id;
    private String name;
    private String destination;
    private List<ExpenseDto> expenses;
    private List<CreateDebtorDto> members;
}
