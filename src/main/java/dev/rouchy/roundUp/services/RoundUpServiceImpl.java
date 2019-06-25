package dev.rouchy.roundUp.services;

import dev.rouchy.roundUp.calculators.CustomerTransactionsRoundUpCalculator;
import dev.rouchy.roundUp.clients.StarlingBankClient;
import dev.rouchy.roundUp.models.*;

import java.util.HashMap;

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
            Amount amount = calculator.roundupTransactions(client.getTransactions(account, request.getSince()));
            var savingGoal = this.client.getSavingGoals(account).stream().filter(s -> s.getName().equals(request.getSavingGoalName())).findFirst().orElseGet(() -> newSavingGoalRequest(account, request));
            var response = this.client.addMoneyToSavingGoal(account, savingGoal, amount);

            var roundUp = new RoundUp(response.getTransferUid(), amount, response.getErrors());
            map.put(account.getAccountUid(), roundUp);
        }

        var response = new RoundUpResponse();
        response.setRoundUps(map);
        return response;
    }

    private SavingGoal newSavingGoalRequest(Account account, RoundUpRequest roundUpRequest) {
        NewSavingGoalRequest request = new NewSavingGoalRequest();
        request.setCurrency(account.getCurrency());
        request.setName(roundUpRequest.getSavingGoalName());
        Amount target = new Amount();
        target.setCurrency(account.getCurrency());
        target.setMinorUnits(1000_00L);
        request.setTarget(target);

        var response = this.client.createSavingGoal(account, request);

        SavingGoal savingGoal = new SavingGoal();
        savingGoal.setSavingsGoalUid(response.getSavingsGoalUid());
        return savingGoal;
    }
}
