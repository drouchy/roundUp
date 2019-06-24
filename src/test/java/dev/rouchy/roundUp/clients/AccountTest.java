package dev.rouchy.roundUp.clients;


import dev.rouchy.roundUp.Environment;
import dev.rouchy.roundUp.support.FakeStarling;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AccountTest {
    private StarlingBankClient client;


    @BeforeEach
    public void setUp() {
        FakeStarling.ensureStarted();

        client = StarlingBankApiClientBuilder.newBuilder().environment(Environment.DEVELOPMENT).withPort(FakeStarling.PORT).build();
    }

    @Test
    public void fetchesTheAccountsFromTheApi() throws Exception {
        assertEquals(2, client.getAccounts().size());
    }

    @Test
    public void parseTheAccountsFromTheApi() throws Exception {
        var account = client.getAccounts().get(0);

        assertAll("account detail",
                () -> assertEquals("d893985e-2b1e-493c-88c7-46f063d2be7c", account.getAccountUid()),
                () -> assertEquals("57c07432-ad18-4fd5-9a1c-6542e4c4daf5", account.getDefaultCategory())
        );
    }
}