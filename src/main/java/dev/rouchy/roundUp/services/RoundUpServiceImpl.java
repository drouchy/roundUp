package dev.rouchy.roundUp.services;

import dev.rouchy.roundUp.calculators.CustomerTransactionsRoundUpCalculator;
import dev.rouchy.roundUp.clients.StarlingBankClient;
import dev.rouchy.roundUp.models.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class RoundUpServiceImpl implements RoundUpService {
    private final StarlingBankClient client;
    private final CustomerTransactionsRoundUpCalculator calculator;

    public RoundUpServiceImpl(StarlingBankClient client) {
        this.client = client;
        this.calculator = new CustomerTransactionsRoundUpCalculator();
    }

    public RoundUpResponse roundUpCustomerTransactions(RoundUpRequest request) {
        var accounts = client.getAccounts();

        HashMap<String, RoundUp> map = new HashMap<>();

        for (Account account : accounts) {
            Amount amount = calculator.roundupTransactions(client.getTransactions(account));
            var savingGoal = this.client.getSavingGoals(account).stream().filter(s -> s.getName().equals(request.savingGoalName)).findFirst().orElseThrow(RuntimeException::new);
            var response = this.client.addMoneyToSavingGoal(account, savingGoal, amount);

            var roundUp = new RoundUp(response.getTransferUid(), amount, response.getErrors());
            map.put(account.getAccountUid(), roundUp);
        }

        var response = new RoundUpResponse();
        response.setRoundUps(map);
        return response;
    }
}
