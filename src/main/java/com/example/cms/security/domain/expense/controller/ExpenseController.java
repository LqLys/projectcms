package com.example.cms.security.domain.expense.controller;

import com.example.cms.security.domain.expense.dto.UnpaidExpenseDto;
import com.example.cms.security.domain.expense.facade.ExpenseFacade;
import com.example.cms.security.domain.user.entity.UserEntity;
import com.example.cms.security.domain.user.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class ExpenseController {

    private final ExpenseFacade expenseFacade;
    private final UserService userService;

    public ExpenseController(ExpenseFacade expenseFacade, UserService userService) {
        this.expenseFacade = expenseFacade;
        this.userService = userService;
    }


    @GetMapping(path = "/expenses/unpaid")
    public ModelAndView getUsersExpenses(ModelAndView modelAndView) {
        UserEntity authenticatedUser = userService.getAuthenticatedUser();
        List<UnpaidExpenseDto> unpaidExpenses = expenseFacade.getUsersUnpaidExpenses(authenticatedUser);
        return modelAndView;

    }

//    select * from cms_db.expense as e
//    join cms_db.expense_participant as ep on e.id = ep.expense_id
//    join cms_db.users as u on e.user_id = u.id
//    where ep.user_id = 2

}
