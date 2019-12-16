package com.example.cms.security.domain.expense.facade;

import com.example.cms.security.domain.expense.dto.UnpaidExpenseDto;
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
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    public List<UnpaidExpenseDto> getExpensesUserHasToPay(UserEntity authenticatedUser) {
        return expenseService.getExpensesUserHasToPay(authenticatedUser);
    }

    public List<UnpaidExpenseDto> getExpensesOthersHaveToPay(UserEntity authenticatedUser) {
        return expenseService.getExpensesOthersHaveToPay(authenticatedUser);
    }

    public Map<Long, BigDecimal> getAggregatedUserBalance(UserEntity authenticatedUser) {
        List<UnpaidExpenseDto> userHasToPay = expenseService.getExpensesUserHasToPay(authenticatedUser);
        List<UnpaidExpenseDto> othersHaveToPay = expenseService.getExpensesOthersHaveToPay(authenticatedUser);

        Map<Long, List<UnpaidExpenseDto>> userHasToPayAggregated = userHasToPay.stream().collect(Collectors.groupingBy(UnpaidExpenseDto::getUserId));
        Map<Long, List<UnpaidExpenseDto>> othersHaveToPayAggregated = othersHaveToPay.stream().collect(Collectors.groupingBy(UnpaidExpenseDto::getUserId));

        Map<Long, BigDecimal> userHasToPayBalance = userHasToPayAggregated.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey,
                        entry -> entry.getValue().stream().map(UnpaidExpenseDto::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add)));

        Map<Long, BigDecimal> othersHaveToPayBalance = othersHaveToPayAggregated.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey,
                        entry -> entry.getValue().stream().map(UnpaidExpenseDto::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add)));

        Map<Long, BigDecimal> finalUserBalance = userHasToPayBalance.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> othersHaveToPayBalance.getOrDefault(entry.getKey(),BigDecimal.ZERO).subtract(entry.getValue())));


        othersHaveToPayBalance.entrySet().stream().forEach(entry -> {
            if (!finalUserBalance.containsKey(entry.getKey())){
                finalUserBalance.put(entry.getKey(), entry.getValue());
            }
        });
        return finalUserBalance;
//        List<UnpaidExpenseDto> collect = Stream.of(userHasToPay, othersHaveToPay).flatMap(Collection::stream).collect(Collectors.toList());



    }
}
