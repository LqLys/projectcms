package com.example.cms.TMP_ADAM;

import com.example.cms.security.domain.travelgroup.entity.TravelGroupEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ExpenseDivider {

    public class InvalidExpenseException extends RuntimeException {
        public InvalidExpenseException(String errorMessage) {
            super(errorMessage);
        }
    }

    public List<TransferEntity> Split(String title, LocalDateTime timestamp, TravelGroupEntity travelGroup, List<UserInTransfer> participants) {

        Validation(participants);

        var sumOfPayments = participants.stream().map(participant -> participant.initialPayment)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        var paymentPerParticipant = sumOfPayments.divide(BigDecimal.valueOf(participants.size()));

//        var transfers = participants.stream()
//                .map(userInTransfer -> new TransferEntity(title, timestamp, travelGroup, userInTransfer));

        var transfers = new ArrayList<TransferEntity>();
        var transfer = new TransferEntity(title, timestamp, travelGroup, participants.get(0).user,
                participants.get(1).user, participants.get(0).initialPayment.divide(BigDecimal.valueOf(2)));
        transfers.add(transfer);

        return transfers;
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

//    public static int SplitIntoSources<TSource>(
//    IEnumerable<TSource> sources, Func<TSource, bool> validation,
//    Func<TSource, int, int> partialFuncWithRest, int amount, int maxAmountPerSource = int.MaxValue)
//    {
//        var goodSources = sources.Where(validation).ToList();
//
//        while (goodSources.Count > 0 && amount > 0 && maxAmountPerSource > 0)
//        {
//            var sourcesCount = goodSources.Count;
//            var amountPerSource = CalculateAmountPerSource(amount, sourcesCount, maxAmountPerSource);
//            maxAmountPerSource -= amountPerSource;
//
//            if (amountPerSource > 0)
//            {
//                for (var i = sourcesCount - 1; i >= 0 && amount > 0; --i)
//                {
//                    var source = goodSources[i];
//
//                    if (validation(source))
//                    {
//                        amount += partialFuncWithRest(source, amountPerSource);
//                        amount -= amountPerSource;
//                    }
//
//                    if (validation(source) == false)
//                    {
//                        goodSources.RemoveAt(i);
//                    }
//                }
//            }
//        }
//
//        return amount;
//    }
//
//    private static int CalculateAmountPerSource(int amount, int sourcesCount, int maxAmountPerSource)
//    {
//        var amountPerSource = amount / sourcesCount;
//
//        if (amountPerSource * sourcesCount < amount)
//        {
//            ++amountPerSource;
//        }
//
//        amountPerSource = Mathf.Min(amountPerSource, maxAmountPerSource);
//        return amountPerSource;
//    }
}
