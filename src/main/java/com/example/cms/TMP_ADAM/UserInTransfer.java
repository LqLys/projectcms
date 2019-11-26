package com.example.cms.TMP_ADAM;

import com.example.cms.security.domain.user.entity.UserEntity;

import java.math.BigDecimal;

public class UserInTransfer {
    UserEntity user;
    BigDecimal fraction;

    public UserInTransfer(UserEntity user, BigDecimal fraction) {
        this.user = user;
        this.fraction = fraction;
    }
}
