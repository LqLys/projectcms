package com.example.cms.app.domain.expense.controller;

import com.example.cms.app.domain.expense.dto.PayDebtDto;
import com.example.cms.app.domain.expense.dto.UnpaidExpenseDto;
import com.example.cms.app.domain.expense.facade.ExpenseFacade;
import com.example.cms.app.domain.user.entity.UserEntity;
import com.example.cms.app.domain.user.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.util.List;

@Controller
public class ExpenseController {

    private final ExpenseFacade expenseFacade;
    private final UserService userService;

    public ExpenseController(ExpenseFacade expenseFacade, UserService userService) {
        this.expenseFacade = expenseFacade;
        this.userService = userService;
    }


    @GetMapping(path = "/expenses")
    public ModelAndView getUsersExpenses(ModelAndView modelAndView) {
        UserEntity authenticatedUser = userService.getAuthenticatedUser();
        List<UnpaidExpenseDto> userHasToPay = expenseFacade.getExpensesUserHasToPay(authenticatedUser);
        List<UnpaidExpenseDto> othersHaveToPay = expenseFacade.getExpensesOthersHaveToPay(authenticatedUser);
        List<UnpaidExpenseDto>  finalUserBalance = expenseFacade.getAggregatedUserBalance(authenticatedUser);
        final BigDecimal totalBalance = finalUserBalance.stream().map(UnpaidExpenseDto::getAmount).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
        modelAndView.addObject("userHasToPay", userHasToPay);
        modelAndView.addObject("othersHaveToPay", othersHaveToPay);
        modelAndView.addObject("finalUserBalance", finalUserBalance);
        modelAndView.addObject("totalBalance", totalBalance);
        modelAndView.addObject("payDebtDto", new PayDebtDto());
        modelAndView.setViewName("expense/expenses");
        return modelAndView;

    }

    @PostMapping(path = "/expenses/pay")
    public ModelAndView payDebt(PayDebtDto payDebtDto, ModelAndView modelAndView){
        modelAndView.setViewName("redirect:/expenses");
        final UserEntity lender = userService.findUserByEmail(payDebtDto.getLenderEmail());
        final UserEntity debtor = userService.getAuthenticatedUser();
        expenseFacade.payDebt(lender, debtor, payDebtDto.getAmount());
        return modelAndView;

    }

}
