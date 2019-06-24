package dev.rouchy.roundUp.clients;


import dev.rouchy.roundUp.Environment;
import dev.rouchy.roundUp.models.Account;
import dev.rouchy.roundUp.models.Amount;
import dev.rouchy.roundUp.models.NewSavingGoalRequest;
import dev.rouchy.roundUp.models.SavingGoal;
import dev.rouchy.roundUp.support.FakeStarling;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static dev.rouchy.roundUp.support.FakeStarling.PORT;
import static org.junit.jupiter.api.Assertions.*;

class SavingGoalsTest {
    private StarlingBankClient client;

    @BeforeEach
    public void setUp() {
        FakeStarling.ensureStarted();
        client = StarlingBankApiClientBuilder.newBuilder().environment(Environment.DEVELOPMENT).withPort(PORT).build();
    }

    @Test
    public void fetchesTheSavingGoals() throws Exception {
        assertEquals(1, client.getSavingGoals(account()).size());
    }

    @Test
    public void parsesTheSavingGoalsFromTheApi() throws Exception {
        var savingGoal = client.getSavingGoals(account()).get(0);

        assertAll("saving goal detail",
                () -> assertEquals("91958549-f27d-4507-b9cb-c57fc5f22955", savingGoal.getSavingsGoalUid()),
                () -> assertEquals("trip to Paris", savingGoal.getName())
        );
    }

    @Test
    public void addingMoneyToTheSavingGoal() throws Exception {
        Amount amount = new Amount();
        amount.setMinorUnits(1239);
        amount.setCurrency("GBP");

        var response = client.addMoneyToSavingGoal(account(), savingGoal(), amount);

        assertAll("saving goal creation response",
                () -> assertEquals("93220ecc-2b09-4939-935b-6296ad212336", response.getTransferUid()),
                () -> assertTrue(response.isSuccess()),
                () -> assertTrue(response.getErrors().isEmpty())
        );
    }

    @Test
    public void createsANewSavingGoal() throws Exception {
        NewSavingGoalRequest request = new NewSavingGoalRequest();
        request.setName("roundUp goal");
        request.setCurrency("GBP");

        var response = client.createSavingGoal(account(), request);

        assertAll("saving goal creation response",
                () -> assertEquals("bb643772-b708-4129-8e2f-377af4d867b1", response.getSavingsGoalUid()),
                () -> assertTrue(response.isSuccess()),
                () -> assertTrue(response.getErrors().isEmpty())
        );
    }

    private SavingGoal savingGoal() {
        SavingGoal goal = new SavingGoal();
        goal.setSavingsGoalUid("91958549-f27d-4507-b9cb-c57fc5f22955");
        return goal;
    }

    private Account account() {
        var account = new Account();
        account.setAccountUid("ACCOUNT_UID");
        account.setCurrency("GBP");
        return account;
    }

}

