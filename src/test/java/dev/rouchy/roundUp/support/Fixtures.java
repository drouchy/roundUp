package dev.rouchy.roundUp.support;

import dev.rouchy.roundUp.models.Amount;
import dev.rouchy.roundUp.models.TransactionDirection;
import dev.rouchy.roundUp.models.TransactionFeedItem;

import java.util.Scanner;

public class Fixtures {
    public static String load(String resource, String method) {
        var stream = Fixtures.class.getClassLoader().getResourceAsStream(String.format("fixtures/%s/%s.json", resource, method));
        Scanner s = new Scanner(stream).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    public static TransactionFeedItem buildTransactionFor(Long minorUnits, String currency, TransactionDirection direction) {
        TransactionFeedItem item = new TransactionFeedItem();
        Amount amount = new Amount();
        amount.setMinorUnits(minorUnits);
        amount.setCurrency(currency);
        item.setAmount(amount);
        item.setDirection(direction);
        return item;
    }
}
