package com.example.cms.security.domain.expense.repository;

import com.example.cms.security.domain.expense.entity.ExpenseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseRepository extends JpaRepository<ExpenseEntity, Long> {
}
