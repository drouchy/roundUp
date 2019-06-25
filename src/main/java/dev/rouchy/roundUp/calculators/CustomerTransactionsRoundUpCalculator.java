package dev.rouchy.roundUp.calculators;

import dev.rouchy.roundUp.models.Amount;
import dev.rouchy.roundUp.models.TransactionFeedItem;

import java.util.List;

public class CustomerTransactionsRoundUpCalculator {
    private final RoundUpCalculator calculator;

    public CustomerTransactionsRoundUpCalculator() {
        this.calculator = new RoundUpCalculator();
    }

    public Amount roundupTransactions(List<TransactionFeedItem> transactions) {
        if(transactions.isEmpty()) {
            return Amount.zero("GBP");
        }

        var value = transactions.stream()
                .map(calculator::roundUp)
                .map(Amount::getMinorUnits)
                .reduce(0L, (total, amount) -> total + amount);

        Amount amount = new Amount();
        amount.setMinorUnits(value);
        amount.setCurrency(transactions.get(0).getAmount().getCurrency());
        return amount;
    }

}
