package com.example.cms.TMP_ADAM;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ExpenseDividerTest {

    private ExpenseDivider _expenseDivider;
    private LocalDateTime _placehoderDateTime;

    @BeforeAll
    public void OneTimeSetUp(){
        _expenseDivider = new ExpenseDivider();
        _placehoderDateTime = LocalDateTime.of(2000, 10, 20, 2, 3, 0, 0);
    }

    @Test
    public void Will_EmptyParticipantsList_Throw_InvalidExpenseException(){
        assertThrows(ExpenseDivider.InvalidExpenseException.class, () -> {
            _expenseDivider.Split("", _placehoderDateTime, null, new ArrayList<UserInTransfer>());
        });
    }
}