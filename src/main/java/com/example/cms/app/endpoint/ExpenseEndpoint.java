package com.example.cms.app.endpoint;

import com.example.cms.app.domain.expense.facade.ExpenseFacade;
import com.example.cms.app.domain.travelgroup.dto.CreateExpenseParticipants;
import com.example.cms.app.domain.user.entity.UserEntity;
import com.example.cms.app.domain.user.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/expense")
public class ExpenseEndpoint {


    private final ExpenseFacade expenseFacade;
    private final UserService userService;

    public ExpenseEndpoint(ExpenseFacade expenseFacade, UserService userService) {
        this.expenseFacade = expenseFacade;
        this.userService = userService;
    }


    @PostMapping(path = "")
    public void createExpense(@RequestBody CreateExpenseParticipants createExpenseRequest){
        final UserEntity userById = userService.findUserById(createExpenseRequest.getAuthorId());
        expenseFacade.createExpense(createExpenseRequest, userById);
    }

}
