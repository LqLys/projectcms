package com.example.cms.TMP_ADAM;

import com.example.cms.security.domain.travelgroup.entity.TravelGroupEntity;
import com.example.cms.security.domain.user.entity.UserEntity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ExpenseDivider {
    private class Source {
        private UserEntity borrower;
        private BigDecimal amount;
        private BigDecimal fractionError;

        public Source(UserEntity borrower, BigDecimal amount, BigDecimal fractionError) {
            this.borrower = borrower;
            this.amount = amount;
            this.fractionError = fractionError;
        }
    }

    public class InvalidExpenseException extends RuntimeException {
        public InvalidExpenseException(String errorMessage) {
            super(errorMessage);
        }
    }

    private BigDecimal oneHundredth = BigDecimal.ONE.divide(BigDecimal.valueOf(100));

    public List<TransferEntity> Split(String title, LocalDateTime timestamp, TravelGroupEntity travelGroup,
                                      UserInTransfer lender, BigDecimal amount, List<UserInTransfer> borrowers) {

        var participants = new ArrayList<>(borrowers);
        participants.add(lender);

        validateFractionsOf(participants);

        var sources = createSources(amount, participants);
        var rest = calculateRest(amount, sources);
        sources = amortizeRest(sources, rest);

        return ConvertSourcesToTransfers(title, timestamp, travelGroup, lender, sources);
    }

    private void validateFractionsOf(ArrayList<UserInTransfer> participants) {
        var sumOfFractions = participants.stream()
                .map(participant -> participant.getFraction())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (sumOfFractions.compareTo(BigDecimal.ONE) != 0) {
            throw new InvalidExpenseException("Sum of fractions has to equals 1");
        }
    }

    private List<Source> createSources(BigDecimal amount, ArrayList<UserInTransfer> participants) {
        return participants.stream().map(part -> {
            var maxAmount = amount.multiply(part.getFraction());
            var clampedMaxAmount = maxAmount.setScale(2, RoundingMode.FLOOR);
            var error = clampedMaxAmount.divide(amount);
            var fractionError = part.getFraction().subtract(error);

            return new Source(part.getUser(), clampedMaxAmount, fractionError);
        }).collect(Collectors.toList());
    }

    private BigDecimal calculateRest(BigDecimal amount, List<Source> sources) {
        var rest = amount;

        for (var source : sources) {
            rest = rest.subtract(source.amount);
        }
        return rest;
    }

    private List<Source> amortizeRest(List<Source> sources, BigDecimal rest) {
        if (rest.compareTo(BigDecimal.ZERO) > 0) {
            sources = sources.stream().sorted(Comparator.comparing(s -> s.fractionError)).collect(Collectors.toList());

            for (var i = sources.size() - 1; i >= 0 && rest.compareTo(BigDecimal.ZERO) > 0; --i) {
                sources.get(i).amount = sources.get(i).amount.add(oneHundredth);
                rest = rest.subtract(oneHundredth);
            }
        }
        return sources;
    }

    private List<TransferEntity> ConvertSourcesToTransfers(String title, LocalDateTime timestamp, TravelGroupEntity travelGroup, UserInTransfer lender, List<Source> sources) {
        return sources.stream()
                .filter(src -> src.borrower != lender.getUser() && src.amount.compareTo(BigDecimal.ZERO) > 0)
                .map(src -> new TransferEntity(title, timestamp, travelGroup, lender.getUser(), src.borrower, src.amount))
                .collect(Collectors.toList());
    }
}

