package com.example.cms.security.domain.expense.repository;

import com.example.cms.security.domain.expense.entity.ExpenseEntity;
import com.example.cms.security.domain.expense.repository.fragment.ExpenseRepositoryFragment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseRepository extends JpaRepository<ExpenseEntity, Long>, ExpenseRepositoryFragment {
}
