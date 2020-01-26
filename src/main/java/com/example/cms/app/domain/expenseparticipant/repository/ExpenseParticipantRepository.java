package com.example.cms.app.domain.expenseparticipant.repository;

import com.example.cms.app.domain.expenseparticipant.entity.ExpenseParticipantEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseParticipantRepository extends JpaRepository<ExpenseParticipantEntity, Long> {
}
