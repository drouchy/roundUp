package dev.rouchy.roundUp.models;

public class SavingGoal {
    private String savingsGoalUid;
    private String name;
    private Amount target;

    public String getSavingsGoalUid() {
        return savingsGoalUid;
    }

    public void setSavingsGoalUid(String savingsGoalUid) {
        this.savingsGoalUid = savingsGoalUid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Amount getTarget() {
        return target;
    }

    public void setTarget(Amount target) {
        this.target = target;
    }
}
