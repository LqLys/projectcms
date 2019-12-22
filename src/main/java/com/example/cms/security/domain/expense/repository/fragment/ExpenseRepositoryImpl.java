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
                        participation.lender.id,
                        (participation.initialAmount.sum().subtract(participation.paidAmount.sum())),
                        user.email
                ))
                .from(participation).join(user).on(user.id.eq(participation.lender.id))
                .where(participation.lender.id.ne(userId).and(participation.debtor.id.eq(userId)))
                .groupBy(participation.lender.id, user.email)
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
                        (participation.initialAmount.sum().subtract(participation.paidAmount.sum())),
                        user.email
                ))
                .from(participation).join(user).on(user.id.eq(participation.debtor.id))
                .where(participation.lender.id.eq(userId).and(participation.debtor.id.ne(userId)))
                .groupBy(participation.debtor.id, user.email)
                .fetch();


        return fetch;
    }
}
