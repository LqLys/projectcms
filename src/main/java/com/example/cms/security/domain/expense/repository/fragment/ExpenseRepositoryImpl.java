package com.example.cms.security.domain.expense.repository.fragment;

import com.example.cms.security.domain.expense.dto.UnpaidExpenseDto;
import com.example.cms.security.domain.expense.entity.QExpenseEntity;
import com.example.cms.security.domain.expenseparticipant.entity.QExpenseParticipantEntity;
import com.example.cms.security.domain.user.entity.QUserEntity;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import java.util.List;

public class ExpenseRepositoryImpl implements ExpenseRepositoryFragment {

    private final JPAQueryFactory jpaQueryFactory;

    public ExpenseRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }


    @Override
    public List<UnpaidExpenseDto> getExpensesUserHasToPay(Long userId) {
        QUserEntity user = QUserEntity.userEntity;
        QExpenseEntity expense = QExpenseEntity.expenseEntity;
        QExpenseParticipantEntity participation = QExpenseParticipantEntity.expenseParticipantEntity;

        List<UnpaidExpenseDto> fetch = jpaQueryFactory.select(
                Projections.constructor(UnpaidExpenseDto.class,
                        expense.createdBy.id,
                        expense.title,
                        participation.initialAmount,
                        user.email
                ))
                .from(expense).join(participation).on(expense.id.eq(participation.expense.id))
                .join(user).on(expense.createdBy.id.eq(user.id))
                .where(participation.debtor.id.eq(userId))
                .fetch();


        return fetch;
    }

    @Override
    public List<UnpaidExpenseDto> getExpensesOthersHaveToPay(Long userId) {
        QUserEntity user = QUserEntity.userEntity;
        QExpenseEntity expense = QExpenseEntity.expenseEntity;
        QExpenseParticipantEntity participation = QExpenseParticipantEntity.expenseParticipantEntity;

        List<UnpaidExpenseDto> fetch = jpaQueryFactory.select(
                Projections.constructor(UnpaidExpenseDto.class,
                        participation.debtor.id,
                        expense.title,
                        participation.initialAmount,
                        user.email
                ))
                .from(expense).join(participation).on(expense.id.eq(participation.expense.id))
                .join(user).on(participation.debtor.id.eq(user.id))
                .where(participation.debtor.id.ne(userId).and(expense.createdBy.id.eq(userId)))
                .fetch();


        return fetch;
    }
}
