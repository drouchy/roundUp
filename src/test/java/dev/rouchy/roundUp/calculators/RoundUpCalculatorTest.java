package dev.rouchy.roundUp.calculators;

import dev.rouchy.roundUp.models.Amount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Currency;

import static dev.rouchy.roundUp.models.TransactionDirection.IN;
import static dev.rouchy.roundUp.models.TransactionDirection.OUT;
import static dev.rouchy.roundUp.support.Fixtures.buildTransactionFor;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class RoundUpCalculatorTest {
    private static final Currency GBP = Currency.getInstance("GBP");

    private RoundUpCalculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new RoundUpCalculator();
    }

    @Test
    public void theIsNoRoundUpWhenTheTransactionAmountIsRoundAmount() {
        Amount rounded = calculator.roundUp(buildTransactionFor(1000L, "GBP", OUT));

        assertEquals(Long.valueOf(0), rounded.getMinorUnits());
    }

    @Test
    public void roundsUpATransactionToTheNearestPound() {
        Amount rounded = calculator.roundUp(buildTransactionFor(3045L, "GBP", OUT));

        assertAll("roundUp total",
                () -> assertEquals(Long.valueOf(55), rounded.getMinorUnits()),
                () -> assertEquals("GBP", rounded.getCurrency())
        );
    }

    @Test
    public void thereIsNoRoundUpForIncomingTransactions() {
        Amount rounded = calculator.roundUp(buildTransactionFor(3045L, "GBP", IN));

        assertEquals(Long.valueOf(0), rounded.getMinorUnits());
    }
}
