package com.example.cms.app.domain.expense.repository;

import com.example.cms.app.domain.expense.entity.ExpenseEntity;
import com.example.cms.app.domain.expense.repository.fragment.ExpenseRepositoryFragment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseRepository extends JpaRepository<ExpenseEntity, Long>, ExpenseRepositoryFragment {
}
