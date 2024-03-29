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

    /*
        process the user request

┌─────────────────────────────────────────┐
│  Get all user accounts                  │
└───────────────────┬─────────────────────┘                                                                   Λ
                    │                                                                                        ╱ ╲
                    │                                                                                       ╱   ╲
                    │                                                                  .─────────.         ╱     ╲
       ┌────────────▼─────┐                     ┌───────────────────────────────┐    ,'           `.      ╱       ╲                     ┌────────────────┐
       │     account      │───────────────────▶ │    Get account savingGoals    │──▶(Filter by name )───▶▕  Found? ▏───────yes─────────▶│ saving goal    │
       └─┬────────────────┴─┐                   └───────────────────────────────┘    '─.         ,─'      ╲       ╱                     └────────────────┘
         │     account      │                         ┌──────────────────┐              `───────'          ╲     ╱                               │
         └─┬────────────────┴─┐                       │   Saving Goal    │                                  ╲   ╱                                │
           │     account      │                       └─┬────────────────┴─┐                                 ╲ ╱                                 │
           └──────────────────┘                         │   Saving Goal    │                                  │                                  │
                                                        └─┬────────────────┴─┐                                │                                  │
                                                          │   Saving Goal    │                               no                                  │
                                                          └──────────────────┘                                │                                  │
                                                                                                              │                                  │
                                                                                                              ▼                     ┌────────────┘
                                                                                                     ┌─────────────────────────┐    │
                                                                                                     │ create new saving goal  │    │
                                                                                                     └─────────────────────────┘    │
                                                                                                                 │                  │
                                                                                                                 │                  │
                                                                                                                 │                  │
                                                                                                                 │                  │
                                                                                                                 ▼                  ▼
                                                                                                            ┌───────────────────────────┐
                                                                                                            │                           │
                                                                                                            │ calculate round up amount │
                                                                                                            │                           │
                                                                                                            └─────────────┬─────────────┘
                                                                                                                          │
                                                                                                                          ▼
                                                                                                            ┌───────────────────────────┐
                                                                                                            │                           │
                                                                                                            │  send money to saving goal│
                                                                                                            │                           │
                                                                                                            └───────────────────────────┘
     */
    public RoundUpResponse roundUpCustomerTransactions(RoundUpRequest request) {
        var accounts = client.getAccounts();

        HashMap<String, RoundUp> map = new HashMap<>();

        for (Account account : accounts) {
            Amount amount = calculator.roundupTransactions(client.getTransactions(account, request.getSince()));
            var savingGoal = findOrCreateSavingGoal(account, request.getSavingGoalName());
            var response = this.client.addMoneyToSavingGoal(account, savingGoal, amount);

            var roundUp = new RoundUp(response.getTransferUid(), amount, response.getErrors());
            map.put(account.getAccountUid(), roundUp);
        }

        var response = new RoundUpResponse();
        response.setRoundUps(map);
        return response;
    }

    /*
        find the first saving goal with a specific name.
        if not found it creates a new one with a 1000 (currency) target
     */
    private SavingGoal findOrCreateSavingGoal(Account account, String goalName) {
        return this.client.getSavingGoals(account)
                .stream()
                .filter(s -> s.getName().equals(goalName))
                    .findFirst()
                    .orElseGet(() -> newSavingGoalRequest(account, goalName));
    }

    /*
        build a new saving goal.

        it is used when the saving goal requested by the user could not be found
     */
    private SavingGoal newSavingGoalRequest(Account account, String goalName) {
        NewSavingGoalRequest request = new NewSavingGoalRequest();
        request.setCurrency(account.getCurrency());
        request.setName(goalName);
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
