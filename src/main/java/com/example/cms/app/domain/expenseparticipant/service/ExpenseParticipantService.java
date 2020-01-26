package com.example.cms.app.domain.expenseparticipant.service;

import com.example.cms.app.domain.expenseparticipant.repository.ExpenseParticipantRepository;
import org.springframework.stereotype.Service;

@Service
public class ExpenseParticipantService {

    private final ExpenseParticipantRepository expenseParticipantRepository;

    public ExpenseParticipantService(ExpenseParticipantRepository expenseParticipantRepository) {
        this.expenseParticipantRepository = expenseParticipantRepository;
    }
}
