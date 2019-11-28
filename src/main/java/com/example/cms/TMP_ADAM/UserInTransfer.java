package com.example.cms.TMP_ADAM;

import com.example.cms.security.domain.user.entity.UserEntity;

import java.math.BigDecimal;

public class UserInTransfer {
    private UserEntity user;
    private BigDecimal fraction;

    public UserInTransfer(UserEntity user, BigDecimal fraction) {
        this.user = user;
        this.fraction = fraction;
    }

    public UserEntity getUser() {
        return user;
    }

    public BigDecimal getFraction() {
        return fraction;
    }
}
