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

    public TransferEntity(String title, LocalDateTime timestamp, TravelGroupEntity travelGroup, UserInTransfer userInTransfer) {
        title = title;
        dateTime = timestamp;
        travelGroupEntity = travelGroup;

    }
}
