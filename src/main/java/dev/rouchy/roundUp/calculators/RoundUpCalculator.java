package dev.rouchy.roundUp.calculators;

import dev.rouchy.roundUp.models.Amount;
import dev.rouchy.roundUp.models.TransactionDirection;
import dev.rouchy.roundUp.models.TransactionFeedItem;

import java.math.BigDecimal;
import java.math.MathContext;

/*
    Calculate the amount we need to round Up single transaction
        * an eligible transaction is an OUT transaction
        * a non eligible transaction has a round up amount of 0

    The round up amount is the difference between the ceiling of the amount and the actual amount

        i.e. 3.53 GBP -> ceiling 4.00 GBP => difference 0.47 GBP

    Note: as the transaction are in the minor unit (pence, cents...) we do need to convert in the major unit first, and
    then in the minor units back.
 */
public class RoundUpCalculator {
    public Amount roundUp(TransactionFeedItem transaction) {
        if(transaction.getDirection() == TransactionDirection.IN) {
            return Amount.zero(transaction.getAmount().getCurrency());
        }

        Amount amount = new Amount();
        amount.setMinorUnits(roundUpValue(transaction.getAmount().getMinorUnits()));
        amount.setCurrency(transaction.getAmount().getCurrency());
        return amount;
    }

    private Long roundUpValue(Long minorUnits) {
        var amount = minorUnits / 100.0;
        var ceiling = Math.ceil(amount);

        var difference = ceiling - amount;
        return Math.round(difference * 100);
    }
}
