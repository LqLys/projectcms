package com.example.cms.security.domain.expense.entity;


import com.example.cms.security.domain.user.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "EXPENSE")
public class ExpenseEntity {

    private static final String SEQ_EXPENSE = "SEQ_EXPENSE";
    @SequenceGenerator(name = SEQ_EXPENSE, sequenceName = SEQ_EXPENSE, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ_EXPENSE)
    @Column(name = "ID")
    @Id
    private Long id;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "AMOUNT")
    private BigDecimal amount;

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private UserEntity createdBy;



}
