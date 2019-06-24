package dev.rouchy.roundUp.clients;

import dev.rouchy.roundUp.Environment;
import dev.rouchy.roundUp.models.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static dev.rouchy.roundUp.models.TransactionDirection.OUT;
import static dev.rouchy.roundUp.models.TransactionSource.INTERNAL_TRANSFER;
import static dev.rouchy.roundUp.models.TransactionStatus.SETTLED;
import static dev.rouchy.roundUp.support.FakeStarling.PORT;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TransactionsTest {
    private StarlingBankClient client;

    @BeforeEach
    public void setUp() {
        client = StarlingBankApiClientBuilder.newBuilder().environment(Environment.DEVELOPMENT).withPort(PORT).build();
    }

    @Test
    public void fetchesTheTransactionsForTheAccountAndTheCategory() throws Exception {
        assertEquals(38, client.getTransactions(account()).size());
    }

    @Test
    public void parsesTheTransactionsInTheResponseFromTheApi() throws Exception {
        var transaction = client.getTransactions(account()).get(0);

        assertAll("transaction detail",
                () -> assertEquals(OUT, transaction.getDirection()),
                () -> assertEquals(SETTLED, transaction.getStatus()),
                () -> assertEquals(INTERNAL_TRANSFER, transaction.getSource()),
                () -> assertEquals(Integer.valueOf(1), transaction.getAmount().getMinorUnits()),
                () -> assertEquals("GBP", transaction.getAmount().getCurrency())
        );
    }

    private Account account() {
        var account = new Account();
        account.setAccountUid("ACCOUNT_UID");
        account.setDefaultCategory("CATEGORY_UID");
        return account;
    }

}