package dev.rouchy.roundUp.support;

import spark.Service;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static spark.Service.ignite;

public class FakeStarling {
    public static final int PORT = 8888;
    private static Service http;

    static {
        http = ignite().port(PORT);
    }

    public static void ensureStarted() {
        http.get("/api/v2/accounts", (req, res) -> Fixtures.load("accounts", "index"));
        http.get("/api/v2/feed/account/:accountUid/category/:categoryUid", (req, res) -> Fixtures.load("transactions", "index"));
        http.get("/api/v2/account/:accountUid/savings-goals", (req, res) -> Fixtures.load("saving_goals", "index"));
        http.put("/api/v2/account/:accountUid/savings-goals/:savingGoalUis/add-money/:transactionUid", (req, res) -> Fixtures.load("saving_goals", "add_money"));
        http.put("/api/v2/account/:accountUid/savings-goals", (req, res) -> {
            checkSavingGoal(req.body()) ;
            return Fixtures.load("saving_goals", "new");
        });
    }

    private static void checkSavingGoal(String body) {
        assertTrue(body.contains("roundUp goal"));
        assertTrue(body.contains("GBP"));
    }
}
