package com.example.cms.security.domain.expenseparticipant.repository;

import com.example.cms.security.domain.expenseparticipant.entity.ExpenseParticipantEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseParticipantRepository extends JpaRepository<ExpenseParticipantEntity, Long> {
}
