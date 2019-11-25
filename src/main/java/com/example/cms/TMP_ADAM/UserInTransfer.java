package com.example.cms.TMP_ADAM;

import com.example.cms.security.domain.user.entity.UserEntity;

import java.math.BigDecimal;

public class UserInTransfer {
    UserEntity user;
    BigDecimal initialPayment;
    BigDecimal fraction;

    public UserInTransfer(UserEntity user, BigDecimal initialPayment, BigDecimal fraction) {
        this.user = user;
        this.initialPayment = initialPayment;
        this.fraction = fraction;
    }
}
