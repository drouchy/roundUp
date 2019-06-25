package dev.rouchy.roundUp.calculators;

import dev.rouchy.roundUp.models.Amount;
import dev.rouchy.roundUp.models.TransactionDirection;
import dev.rouchy.roundUp.models.TransactionFeedItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static dev.rouchy.roundUp.models.TransactionDirection.IN;
import static dev.rouchy.roundUp.models.TransactionDirection.OUT;
import static dev.rouchy.roundUp.support.Fixtures.buildTransactionFor;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CustomerTransactionsRoundUpCalculatorTest {
    private CustomerTransactionsRoundUpCalculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new CustomerTransactionsRoundUpCalculator();
    }

    @Test
    public void thereIsNoRoundUpMoneyWhenThereIsNoTransactions() {
        var amount = calculator.roundupTransactions(emptyList());

        assertEquals(Long.valueOf(0), amount.getMinorUnits());
    }

    @Test
    public void sumsUPAllTheOutTransactions() {
        var amount = calculator.roundupTransactions(asList(
                buildTransactionFor(435L, "GBP", OUT),
                buildTransactionFor(33L, "GBP", IN),
                buildTransactionFor(520L, "GBP", OUT),
                buildTransactionFor(87L, "GBP", OUT)
        ));

        assertAll("roundUp total",
                () -> assertEquals(Long.valueOf(158), amount.getMinorUnits()),
                () -> assertEquals("GBP", amount.getCurrency())
        );
    }

    @Test
    public void thereIsNoRoundUpForOnlyIncomingTransactions() {
        var amount = calculator.roundupTransactions(asList(
                buildTransactionFor(44L, "GBP", IN),
                buildTransactionFor(33L, "GBP", IN)
        ));

        assertEquals(Long.valueOf(0), amount.getMinorUnits());
    }

}