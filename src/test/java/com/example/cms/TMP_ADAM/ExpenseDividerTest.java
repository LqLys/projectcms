package com.example.cms.TMP_ADAM;

import com.example.cms.security.domain.user.entity.UserEntity;
import com.mysema.commons.lang.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ExpenseDividerTest {

    private static ExpenseDivider _expenseDivider;
    private static LocalDateTime _placehoderDateTime;

    @BeforeAll
    public static void OneTimeSetUp() {
        _expenseDivider = new ExpenseDivider();
        _placehoderDateTime = LocalDateTime.of(2000, 10, 20, 2, 3, 0, 0);
    }

    @Test
    public void Will_EmptyParticipantsList_Throw_InvalidExpenseException() {
        assertThrows(ExpenseDivider.InvalidExpenseException.class, () -> {
            _expenseDivider.Split("", _placehoderDateTime, null, new ArrayList<UserInTransfer>());
        });
    }

    @Test
    public void Will_100_BeEquallySplit_Into_Two_Users() {
        var user1 = new UserEntity();
        var user2 = new UserEntity();

        var usersInTransfer = new ArrayList<UserInTransfer>();
        usersInTransfer.add(new UserInTransfer(user1, BigDecimal.valueOf(100), BigDecimal.valueOf(0.5)));
        usersInTransfer.add(new UserInTransfer(user2, BigDecimal.valueOf(0), BigDecimal.valueOf(0.5)));

        var transfers = _expenseDivider.Split("", _placehoderDateTime, null, usersInTransfer);

        Assert.isTrue(transfers.size() == 1, "There should be only one transfer");
        Assert.isTrue(transfers.get(0).getLender().equals(user1), "Lender should be user1");
        Assert.isTrue(transfers.get(0).getBorrower().equals(user1), "Borrower should be user2");
        Assert.isTrue(transfers.get(0).getAmount().compareTo(BigDecimal.valueOf(50)) == 0, "Value should equals 50");
    }
}