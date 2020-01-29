package com.example.cms.app.domain.expense.service;

import com.example.cms.app.domain.expense.dto.UnpaidExpenseDto;
import com.example.cms.app.domain.expense.entity.ExpenseEntity;
import com.example.cms.app.domain.expense.repository.ExpenseRepository;
import com.example.cms.app.domain.expenseparticipant.entity.ExpenseParticipantEntity;
import com.example.cms.app.domain.expenseparticipant.repository.ExpenseParticipantRepository;
import com.example.cms.app.domain.user.entity.UserEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final ExpenseParticipantRepository expenseParticipantRepository;

    public ExpenseService(ExpenseRepository expenseRepository, ExpenseParticipantRepository expenseParticipantRepository) {
        this.expenseRepository = expenseRepository;
        this.expenseParticipantRepository = expenseParticipantRepository;
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

    public void payDebt(UserEntity lender, UserEntity debtor, BigDecimal amount) {
        final List<ExpenseParticipantEntity> unpaidExpenses = expenseRepository.getUnpaidExpenses(lender.getId(), debtor.getId());
        BigDecimal remaining = new BigDecimal(amount.toString());
        for(int i = 0; i < unpaidExpenses.size(); i++){
            final ExpenseParticipantEntity expParticipant = unpaidExpenses.get(i);
            final BigDecimal toBePaid = expParticipant.getInitialAmount().subtract(expParticipant.getPaidAmount());
            if(firstGreaterOrEqual(remaining, toBePaid)){
                remaining = remaining.subtract(toBePaid);
                expParticipant.setPaidAmount(expParticipant.getPaidAmount().add(toBePaid));
            }else{
                expParticipant.setPaidAmount(expParticipant.getPaidAmount().add(remaining));
                remaining = BigDecimal.ZERO;
            }
        }
        expenseParticipantRepository.saveAll(unpaidExpenses);
    }


    private boolean firstGreaterOrEqual(BigDecimal first, BigDecimal second){
        return first.compareTo(second) > -1;
    }
}
