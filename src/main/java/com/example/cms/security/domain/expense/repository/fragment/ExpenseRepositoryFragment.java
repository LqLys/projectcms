package com.example.cms.security.domain.expense.repository.fragment;

import com.example.cms.security.domain.expense.dto.UnpaidExpenseDto;
import com.example.cms.security.domain.expenseparticipant.entity.ExpenseParticipantEntity;

import java.util.List;

public interface ExpenseRepositoryFragment {


    List<UnpaidExpenseDto> getExpensesUserHasToPay(Long userId);
    List<UnpaidExpenseDto> getExpensesOthersHaveToPay(Long userId);
    List<ExpenseParticipantEntity> getUnpaidExpenses(Long lenderId, Long debtorId);
}
