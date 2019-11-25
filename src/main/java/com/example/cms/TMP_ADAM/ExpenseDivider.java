package com.example.cms.TMP_ADAM;

import com.example.cms.security.domain.travelgroup.entity.TravelGroupEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class ExpenseDivider {

    public class InvalidExpenseException extends RuntimeException {
        public InvalidExpenseException(String errorMessage) {
            super(errorMessage);
        }
    }

    public void Split(String title, LocalDateTime timestamp, TravelGroupEntity travelGroup, List<UserInTransfer> participants) {

        Validation(participants);

        var sumOfPayments = participants.stream().map(participant -> participant.initialPayment)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        var paymentPerParticipant = sumOfPayments.divide(BigDecimal.valueOf(participants.size()));

        var transfers = participants.stream()
                .map(userInTransfer -> new TransferEntity(title, timestamp, travelGroup, userInTransfer));

    }

    private void Validation(List<UserInTransfer> participants) {
        if (SumOfFractionsNotEqualsOne(participants)) {
            throw new InvalidExpenseException("Sum of fractions has to equals 1");
        }
    }

    private boolean SumOfFractionsNotEqualsOne(List<UserInTransfer> participants) {
        var sumOfFractions = participants.stream()
                .map(participant -> participant.fraction)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return sumOfFractions.compareTo(BigDecimal.ONE) != 0;
    }
}
