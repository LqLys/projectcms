package com.example.cms.TMP_ADAM;

import com.example.cms.security.domain.travelgroup.entity.TravelGroupEntity;
import com.example.cms.security.domain.user.entity.UserEntity;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ExpenseDividerTest {

    private static ExpenseDivider _expenseDivider;
    private static LocalDateTime _placeholderDateTime;

    @BeforeAll
    public static void OneTimeSetUp() {
        _expenseDivider = new ExpenseDivider();
        _placeholderDateTime = LocalDateTime.of(2000, 10, 20, 2, 3, 0, 0);
    }

    @Test
    public void _00_Will_EmptyParticipantsList_Throw_InvalidExpenseException() {
        assertThrows(ExpenseDivider.InvalidExpenseException.class, () -> {
            var lender = new UserInTransfer(new UserEntity(), BigDecimal.valueOf(0.5));
            _expenseDivider.Split("", _placeholderDateTime, null, lender, BigDecimal.ONE, new ArrayList<>());
        });
    }

    @Test
    public void _01_Will_100_BeEquallySplit_Into_Two_Users() {
        var user1 = new UserEntity();
        var user2 = new UserEntity();

        var borrowers = new ArrayList<UserInTransfer>();
        var lender = new UserInTransfer(user1, BigDecimal.valueOf(0.5));
        borrowers.add(new UserInTransfer(user2, BigDecimal.valueOf(0.5)));

        var transfers = _expenseDivider.Split("", _placeholderDateTime, null, lender, BigDecimal.valueOf(100), borrowers);

        assertEquals(1, transfers.size(), "Invalid number of transfers");
        assertTransfer(transfers.get(0), user1, user2, BigDecimal.valueOf(50).setScale(2));
    }

    private void assertTransfer(TransferEntity transfer, UserEntity user1, UserEntity user2, BigDecimal expectedAmount) {
        assertEquals(user1, transfer.getLender(), "Lender should be user1");
        assertEquals(user2, transfer.getBorrower(), "Borrower should be user2");
        assertEquals(expectedAmount, transfer.getAmount(), "Invalid transfer amount");
    }

    @Test
    public void _02_Will_0_10_BeSplit_Into_Three_Users() {
        var user1 = new UserEntity();
        var user2 = new UserEntity();
        var user3 = new UserEntity();

        var borrowers = new ArrayList<UserInTransfer>();
        var lender = new UserInTransfer(user1, BigDecimal.valueOf(0.33));
        borrowers.add(new UserInTransfer(user2, BigDecimal.valueOf(0.33)));
        borrowers.add(new UserInTransfer(user3, BigDecimal.valueOf(0.34)));

        var transfers = _expenseDivider.Split("", _placeholderDateTime, null, lender, BigDecimal.valueOf(0.1), borrowers);

        assertEquals(2, transfers.size(), "Invalid number of transfers");

        assertTransfer(transfers.get(0), user1, user2, BigDecimal.valueOf(0.03));
        assertTransfer(transfers.get(1), user1, user3, BigDecimal.valueOf(0.04));
    }

    @Test
    public void _03_Will_TransferData_BeCorrectlyFilled() {
        var travelGroup = new TravelGroupEntity();
        var user1 = new UserEntity();
        var user2 = new UserEntity();

        var borrowers = new ArrayList<UserInTransfer>();
        var lender = new UserInTransfer(user1, BigDecimal.valueOf(0.5));
        borrowers.add(new UserInTransfer(user2, BigDecimal.valueOf(0.5)));

        var transfers = _expenseDivider.Split("Banana", _placeholderDateTime, travelGroup, lender, BigDecimal.valueOf(100), borrowers);

        var transfer = transfers.get(0);
        assertEquals("Banana", transfer.getTitle());
        assertEquals(BigDecimal.valueOf(50).setScale(2), transfer.getAmount());
        assertEquals(travelGroup, transfer.getTravelGroupEntity());
        assertEquals(_placeholderDateTime, transfer.getDateTime());
        assertEquals(user1, transfer.getLender());
        assertEquals(user2, transfer.getBorrower());
    }
}