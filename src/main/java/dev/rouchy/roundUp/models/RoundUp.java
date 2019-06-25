package dev.rouchy.roundUp.models;

import java.util.List;

public class RoundUp {
    private final String transactionUid;
    private final Amount amount;
    private final List<Error> errors;

    public RoundUp(String transactionUid, Amount amount, List<Error> errors) {
        this.transactionUid = transactionUid;
        this.amount = amount;
        this.errors = errors;
    }

    public String getTransactionUid() {
        return transactionUid;
    }

    public Amount getAmount() {
        return amount;
    }

    public List<Error> getErrors() {
        return errors;
    }
}
