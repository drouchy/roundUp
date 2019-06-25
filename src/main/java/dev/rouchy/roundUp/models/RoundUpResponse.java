package dev.rouchy.roundUp.models;

import java.util.Map;

public class RoundUpResponse {
    private Map<String, RoundUp> roundUps;

    public void setRoundUps(Map<String, RoundUp> roundUps) {
        this.roundUps = roundUps;
    }

    public Map<String, RoundUp> getRoundUps() {
        return roundUps;
    }
}
