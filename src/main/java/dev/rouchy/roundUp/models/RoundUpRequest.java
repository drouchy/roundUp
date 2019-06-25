package dev.rouchy.roundUp.models;

import java.time.Instant;

public class RoundUpRequest {
    private String savingGoalName;
    private Instant since;

    public String getSavingGoalName() {
        return savingGoalName;
    }

    public void setSavingGoalName(String savingGoalName) {
        this.savingGoalName = savingGoalName;
    }

    public Instant getSince() {
        return since;
    }

    public void setSince(Instant since) {
        this.since = since;
    }
}
