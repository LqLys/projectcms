package com.example.cms.security.domain.expenseparticipant.entity;


import com.example.cms.security.domain.expense.entity.ExpenseEntity;
import com.example.cms.security.domain.user.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "EXPENSE_PARTICIPANT")
public class ExpenseParticipantEntity {

    private static final String SEQ_EXPENSE_PARTICIPANT = "SEQ_EXPENSE_PARTICIPANT";
    @SequenceGenerator(name = SEQ_EXPENSE_PARTICIPANT, sequenceName = SEQ_EXPENSE_PARTICIPANT, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ_EXPENSE_PARTICIPANT)
    @Column(name = "ID")
    @Id
    private Long id;

    @Column(name = "INITIAL_AMOUNT")
    private BigDecimal initialAmount;

    @Column(name = "PAID_AMOUNT")
    private BigDecimal paidAmount;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private UserEntity debtor;

    @ManyToOne
    @JoinColumn(name = "EXPENSE_ID")
    private ExpenseEntity expense;
}
