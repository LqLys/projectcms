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
            var lender = new UserInTransfer(new UserEntity(), BigDecimal.valueOf(0.5));
            _expenseDivider.Split("", _placehoderDateTime, null, lender, BigDecimal.ONE, new ArrayList<>());
        });
    }

    @Test
    public void Will_100_BeEquallySplit_Into_Two_Users() {
        var user1 = new UserEntity();
        var user2 = new UserEntity();

        var borrowers = new ArrayList<UserInTransfer>();
        var lender = new UserInTransfer(user1, BigDecimal.valueOf(0.5));
        borrowers.add(new UserInTransfer(user2, BigDecimal.valueOf(0.5)));

        var transfers = _expenseDivider.Split("", _placehoderDateTime, null, lender, BigDecimal.valueOf(100), borrowers);

        Assert.isTrue(transfers.size() == 1, "There should be only one transfer");
        AssertTransfer(user1, user2, transfers.get(0), BigDecimal.valueOf(50));
    }

    private void AssertTransfer(UserEntity user1, UserEntity user2, TransferEntity transfer, BigDecimal bigDecimal) {
        Assert.isTrue(transfer.getLender().equals(user1), "Lender should be user1");
        Assert.isTrue(transfer.getBorrower().equals(user2), "Borrower should be user2");
        var amount = transfer.getAmount();
        Assert.isTrue(amount.compareTo(bigDecimal) == 0, "Value should equals: "+ bigDecimal +", but equals: " + amount);
    }

    @Test
    public void Will_0_10_BeSplit_Into_Three_Users() {
        var user1 = new UserEntity();
        var user2 = new UserEntity();
        var user3 = new UserEntity();

        var borrowers = new ArrayList<UserInTransfer>();
        var lender = new UserInTransfer(user1, BigDecimal.valueOf(0.33));
        borrowers.add(new UserInTransfer(user2, BigDecimal.valueOf(0.33)));
        borrowers.add(new UserInTransfer(user3, BigDecimal.valueOf(0.34)));

        var transfers = _expenseDivider.Split("", _placehoderDateTime, null, lender, BigDecimal.valueOf(0.1), borrowers);

        Assert.isTrue(transfers.size() == 2, "There should be two transfers");

        AssertTransfer(user1, user2, transfers.get(0), BigDecimal.valueOf(0.03));
        AssertTransfer(user1, user3, transfers.get(1), BigDecimal.valueOf(0.03));
    }
}