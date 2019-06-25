package dev.rouchy.roundUp.models;

public class Amount {
    private Long minorUnits;
    private String currency;

    public Long getMinorUnits() {
        return minorUnits;
    }

    public void setMinorUnits(Long minorUnits) {
        this.minorUnits = minorUnits;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public static Amount zero(String currency) {
        Amount amount = new Amount();
        amount.setCurrency(currency);
        amount.setMinorUnits(0L);
        return amount;
    }
}
