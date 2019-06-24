package dev.rouchy.roundUp.clients;

import dev.rouchy.roundUp.models.*;

import java.util.List;

public interface StarlingBankClient {
    List<Account> getAccounts() throws ApiError;

    List<TransactionFeedItem> getTransactions(Account account) throws ApiError;

    List<SavingGoal> getSavingGoals(Account account) throws ApiError;

    AddMoneyToSavingGoalResponse addMoneyToSavingGoal(Account account, SavingGoal savingGoal, Amount amount) throws ApiError;

    NewSavingGoalResponse createSavingGoal(Account account, NewSavingGoalRequest request) throws ApiError;

}
