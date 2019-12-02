package com.example.cms.security.domain.expense.service;

import com.example.cms.security.domain.expense.entity.ExpenseEntity;
import com.example.cms.security.domain.expense.repository.ExpenseRepository;
import org.springframework.stereotype.Service;

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;

    public ExpenseService(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    public void createExpense(ExpenseEntity expense) {
        expenseRepository.save(expense);
    }
}
