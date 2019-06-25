package dev.rouchy.roundUp.calculators;

import dev.rouchy.roundUp.models.Amount;
import dev.rouchy.roundUp.models.TransactionDirection;
import dev.rouchy.roundUp.models.TransactionFeedItem;

import java.math.BigDecimal;
import java.math.MathContext;

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
