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
        public UserEntity borrower;
        public BigDecimal amount;
        public BigDecimal maxAmount;
        public BigDecimal fractionError;

        public Source(UserEntity borrower, BigDecimal maxAmount, BigDecimal fractionError) {
            this.borrower = borrower;
            this.maxAmount = maxAmount;
            this.fractionError = fractionError;
            amount = BigDecimal.ZERO;
        }

        public boolean canStoreMore() {
            return amount.compareTo(maxAmount) < 0;
        }

        public BigDecimal store(BigDecimal amountPerSource) {
            amount = amount.add(amountPerSource);

            var rest = BigDecimal.ZERO;

            if (amount.compareTo(maxAmount) > 0) {
                rest = amount.subtract(maxAmount);
                amount = maxAmount;
            }

            return rest;
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

        var sumOfFractions = participants.stream()
                .map(participant -> participant.fraction)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (sumOfFractions.compareTo(BigDecimal.ONE) != 0) {
            throw new InvalidExpenseException("Sum of fractions has to equals 1");
        }

        var sources = participants.stream().map(part -> {
            var maxAmount = amount.multiply(part.fraction);
            var clampedMaxAmount = maxAmount.setScale(2, RoundingMode.FLOOR);
            var error = clampedMaxAmount.divide(amount);
            var fractionError = part.fraction.subtract(error);

            return new Source(part.user, clampedMaxAmount, fractionError);
        }).collect(Collectors.toList());

        var rest = SplitIntoSources(sources, amount);

        if (rest.compareTo(BigDecimal.ZERO) > 0) {
            sources = sources.stream().sorted(Comparator.comparing(s -> s.fractionError)).collect(Collectors.toList());

            for (var i = sources.size() - 1; i >= 0 && rest.compareTo(BigDecimal.ZERO) > 0; --i) {
                sources.get(i).amount = sources.get(i).amount.add(oneHundredth);
                rest = rest.subtract(oneHundredth);
            }
        }

        return sources.stream()
                .filter(src -> src.borrower != lender.user && src.amount.compareTo(BigDecimal.ZERO) > 0)
                .map(src -> new TransferEntity(title, timestamp, travelGroup, lender.user, src.borrower, src.amount))
                .collect(Collectors.toList());
    }

    private BigDecimal SplitIntoSources(List<Source> sources, BigDecimal amount) {
        var goodSources = sources.stream().filter(s -> s.canStoreMore()).collect(Collectors.toList());

        while (goodSources.size() > 0 && amount.compareTo(BigDecimal.ZERO) > 0) {
            var sourcesCount = goodSources.size();
            var amountPerSource = CalculateAmountPerSource(amount, sourcesCount);

            if (amountPerSource.compareTo(BigDecimal.ZERO) > 0) {
                for (var i = sourcesCount - 1; i >= 0 && amount.compareTo(BigDecimal.ZERO) > 0; --i) {
                    var source = goodSources.get(i);

                    if (source.canStoreMore()) {
                        amount = amount.add(source.store(amountPerSource));
                        amount = amount.subtract(amountPerSource);
                    }

                    if (source.canStoreMore() == false) {
                        goodSources.remove(i);
                    }
                }
            }
        }

        return amount;
    }

    private BigDecimal CalculateAmountPerSource(BigDecimal amount, int sourcesCount) {
        var count = BigDecimal.valueOf(sourcesCount);
        var hundred = BigDecimal.valueOf(100);
        var amountPerSource = amount.multiply(hundred).divideToIntegralValue(count).divide(hundred);

        if (amountPerSource.multiply(count).compareTo(amount) < 0) {
            amountPerSource.add(oneHundredth);
        }

        return amountPerSource;
    }
}
