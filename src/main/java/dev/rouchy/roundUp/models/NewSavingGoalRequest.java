package dev.rouchy.roundUp.models;

public class NewSavingGoalRequest {
    private String name;
    private String currency;
    private Amount target;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Amount getTarget() {
        return target;
    }

    public void setTarget(Amount target) {
        this.target = target;
    }
}
