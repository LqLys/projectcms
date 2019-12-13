package com.example.cms.security.domain.expense.repository.fragment;

import com.example.cms.security.domain.expense.dto.UnpaidExpenseDto;

import java.util.List;

public interface ExpenseRepositoryFragment {


    List<UnpaidExpenseDto> getExpensesUserHasToPay(Long userId);
    List<UnpaidExpenseDto> getExpensesOthersHaveToPay(Long userId);
}
