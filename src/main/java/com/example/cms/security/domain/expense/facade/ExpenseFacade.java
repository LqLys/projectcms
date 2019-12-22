package com.example.cms.security.domain.expense.facade;

import com.example.cms.security.domain.expense.dto.UnpaidExpenseDto;
import com.example.cms.security.domain.expense.entity.ExpenseEntity;
import com.example.cms.security.domain.expense.service.ExpenseService;
import com.example.cms.security.domain.expenseparticipant.entity.ExpenseParticipantEntity;
import com.example.cms.security.domain.expenseparticipant.service.ExpenseParticipantService;
import com.example.cms.security.domain.travelgroup.dto.CreateExpenseParticipants;
import com.example.cms.security.domain.travelgroup.dto.CreateExpenseRequest;
import com.example.cms.security.domain.travelgroup.dto.ExpenseParticipantDto;
import com.example.cms.security.domain.travelgroup.service.TravelGroupService;
import com.example.cms.security.domain.user.entity.UserEntity;
import com.example.cms.security.domain.user.service.UserService;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
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

    public void createExpense(CreateExpenseParticipants createExpenseRequest, UserEntity authenticatedUser) {

        final BigDecimal averageAmount = createExpenseRequest.getTotalAmount()
                .divide(BigDecimal.valueOf(createExpenseRequest.getDebtors().size() + 1), RoundingMode.HALF_UP);
        final BigDecimal debtorsInput = createExpenseRequest.getDebtors().stream()
                .map(ExpenseParticipantDto::getAmount)
                .reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
        final BigDecimal authorsInput = createExpenseRequest.getTotalAmount().subtract(debtorsInput);
        final List<ExpenseParticipantDto> expenseParticipantDtos = new ArrayList<>(createExpenseRequest.getDebtors());
        expenseParticipantDtos.add(new ExpenseParticipantDto(authenticatedUser.getId(), authorsInput));
        expenseParticipantDtos.forEach(p-> p.setAmount(averageAmount.subtract(p.getAmount())));
        final List<Long> memberIds = expenseParticipantDtos.stream()
                .map(ExpenseParticipantDto::getDebtorId)
                .collect(Collectors.toList());
        final Map<Long, UserEntity> members = userService.findAllByIdIn(memberIds).stream()
                .collect(Collectors.toMap(UserEntity::getId, Function.identity()));
        final List<ExpenseParticipantDto> participantsBelowAverage = expenseParticipantDtos.stream()
                .filter(p -> p.getAmount().compareTo(BigDecimal.ZERO) == 1)
                .collect(Collectors.toList());
        final List<ExpenseParticipantDto> participantsAboveAverage = expenseParticipantDtos.stream()
                .filter(p -> p.getAmount().compareTo(BigDecimal.ZERO) == -1)
                .collect(Collectors.toList());
        final int nrOfAbove = participantsAboveAverage.size();



        ExpenseEntity expense = ExpenseEntity.builder()
                .amount(createExpenseRequest.getTotalAmount())
                .title(createExpenseRequest.getTitle())
                .createdAt(LocalDateTime.now())
                .createdBy(authenticatedUser)
                .group(travelGroupService.findTravelGroup(createExpenseRequest.getGroupId()))
                .build();

        final List<ExpenseParticipantEntity> expenseParticipations = participantsBelowAverage.stream()
                .map(pBelow -> participantsAboveAverage.stream()
                        .map(pAbove -> mapToExpenseParticipant(pAbove.getDebtorId(), pBelow.getDebtorId(),
                                getParticipationAmount(pBelow.getAmount(), nrOfAbove), expense,
                                members)).collect(Collectors.toList())).flatMap(Collection::stream).collect(Collectors.toList());
        expense.setDebtors(expenseParticipations);
        expenseService.createExpense(expense);
    }
    private BigDecimal getParticipationAmount(BigDecimal amountBelowAvg, int nrOfMembersAbove){
        int denominator = nrOfMembersAbove > 0 ? nrOfMembersAbove : 1;
        return amountBelowAvg.divide(BigDecimal.valueOf(denominator), RoundingMode.HALF_UP);
    }

    private ExpenseParticipantEntity mapToExpenseParticipant(Long lenderId, Long debtorId, BigDecimal amount, ExpenseEntity expense,
                                                             Map<Long, UserEntity> members){
        return ExpenseParticipantEntity.builder()
                .lender(members.get(lenderId))
                .debtor(members.get(debtorId))
                .initialAmount(amount)
                .paidAmount(BigDecimal.ZERO)
                .expense(expense)
                .build();
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

    public List<UnpaidExpenseDto> getAggregatedUserBalance(UserEntity authenticatedUser) {
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
        return finalUserBalance.entrySet().stream()
                .map(entry -> new UnpaidExpenseDto(entry.getKey(), entry.getValue(), getUserEmail(entry.getKey()))).collect(Collectors.toList());

    }

    private String getUserEmail(Long userId) {
        final UserEntity userById = userService.findUserById(userId);
        return userById.getEmail();
    }
}
