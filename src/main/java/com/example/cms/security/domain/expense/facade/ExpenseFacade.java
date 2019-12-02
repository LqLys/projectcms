package com.example.cms.security.domain.expense.facade;

import com.example.cms.security.domain.expense.entity.ExpenseEntity;
import com.example.cms.security.domain.expense.service.ExpenseService;
import com.example.cms.security.domain.expenseparticipant.entity.ExpenseParticipantEntity;
import com.example.cms.security.domain.expenseparticipant.service.ExpenseParticipantService;
import com.example.cms.security.domain.travelgroup.dto.CreateExpenseRequest;
import com.example.cms.security.domain.travelgroup.service.TravelGroupService;
import com.example.cms.security.domain.user.entity.UserEntity;
import com.example.cms.security.domain.user.service.UserService;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ExpenseFacade {

    private final ExpenseService expenseService;
    private final TravelGroupService travelGroupService;
    private final ExpenseParticipantService expenseParticipantService;
    private final UserService userService;

    public ExpenseFacade(ExpenseService expenseService, TravelGroupService travelGroupService, ExpenseParticipantService expenseParticipantService, UserService userService) {
        this.expenseService = expenseService;
        this.travelGroupService = travelGroupService;
        this.expenseParticipantService = expenseParticipantService;
        this.userService = userService;
    }

    public void createExpense(CreateExpenseRequest createExpenseRequest, UserEntity authenticatedUser) {
        List<UserEntity> users = userService.findAllByIdIn(createExpenseRequest.getDebtors());
        BigDecimal participationAmount = createExpenseRequest.getAmount()
                .divide(BigDecimal.valueOf(users.size()+1), 2, RoundingMode.HALF_UP);


        ExpenseEntity expense = ExpenseEntity.builder()
                .amount(createExpenseRequest.getAmount())
                .title(createExpenseRequest.getTitle())
                .createdAt(LocalDateTime.now())
                .createdBy(authenticatedUser)
                .group(travelGroupService.findTravelGroup(createExpenseRequest.getGroupId()))
                .build();
        List<ExpenseParticipantEntity> expenseParticipants =
                users.stream().map(user -> userToExpenseParticipant(user, participationAmount, expense)).collect(Collectors.toList());
        expense.setDebtors(expenseParticipants);

        expenseService.createExpense(expense);


    }

    private ExpenseParticipantEntity userToExpenseParticipant(UserEntity debtor, BigDecimal participationAmount, ExpenseEntity expense) {
        return ExpenseParticipantEntity.builder()
                .debtor(debtor)
                .initialAmount(participationAmount)
                .paidAmount(BigDecimal.ZERO)
                .expense(expense)
                .build();
    }
}
