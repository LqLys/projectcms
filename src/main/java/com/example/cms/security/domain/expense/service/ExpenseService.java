package com.example.cms.security.domain.expense.service;

import com.example.cms.security.domain.expense.dto.UnpaidExpenseDto;
import com.example.cms.security.domain.expense.entity.ExpenseEntity;
import com.example.cms.security.domain.expense.repository.ExpenseRepository;
import com.example.cms.security.domain.user.entity.UserEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;

    public ExpenseService(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    public void createExpense(ExpenseEntity expense) {
        expenseRepository.save(expense);
    }

    public List<UnpaidExpenseDto> getExpensesUserHasToPay(UserEntity authenticatedUser) {
        return expenseRepository.getExpensesUserHasToPay(authenticatedUser.getId());

    }

    public List<UnpaidExpenseDto> getExpensesOthersHaveToPay(UserEntity authenticatedUser) {
        return expenseRepository.getExpensesOthersHaveToPay(authenticatedUser.getId());

    }
}
