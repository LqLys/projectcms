package com.example.cms.TMP_ADAM;

import com.example.cms.security.domain.travelgroup.entity.TravelGroupEntity;
import com.example.cms.security.domain.user.entity.UserEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransferEntity {
    private Long id;
    private String title;
    private BigDecimal amount;
    private TravelGroupEntity travelGroupEntity;
    private LocalDateTime dateTime;
    private UserEntity lender;
    private UserEntity borrower;

    public TransferEntity(String title, LocalDateTime timestamp, TravelGroupEntity travelGroup, UserEntity lender,
                          UserEntity borrower, BigDecimal amount) {
        this.title = title;
        dateTime = timestamp;
        travelGroupEntity = travelGroup;
        this.lender = lender;
        this.borrower = borrower;
        this.amount = amount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public UserEntity getLender() {
        return lender;
    }

    public UserEntity getBorrower() {
        return borrower;
    }
}
