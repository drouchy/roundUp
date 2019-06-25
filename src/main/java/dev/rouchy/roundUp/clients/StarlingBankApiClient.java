package dev.rouchy.roundUp.clients;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import dev.rouchy.roundUp.models.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

public class StarlingBankApiClient implements StarlingBankClient {
    private final URI uri;
    private final String token;
    private final HttpClient client;
    private ObjectMapper objectMapper;

    public StarlingBankApiClient(URI baseUri, String token) {
        this.uri = baseUri;
        this.token = token;
        this.client = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).build();
        this.objectMapper = buildObjectMapper();
    }

    public URI baseUri() {
        return this.uri;
    }

    @Override
    public List<Account> getAccounts() throws ApiError {
        try {
            var request = requestBuilder(accountsUri()).GET().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return objectMapper.readValue(response.body(), AccountList.class).getAccounts();
        } catch (InterruptedException | IOException e) {
            throw new ApiError("failed to fetch accounts", e);
        }

    }

    @Override
    public List<TransactionFeedItem> getTransactions(Account account, Instant since) throws ApiError {
        try {
            var request = requestBuilder(transactionsUri(account, since)).GET().build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            return objectMapper.readValue(response.body(), TransactionFeed.class).getFeedItems();
        } catch (InterruptedException | IOException e) {
            throw new ApiError("failed to fetch transactions feed", e);
        }
    }

    @Override
    public List<SavingGoal> getSavingGoals(Account account) throws ApiError {
        try {
            var request = requestBuilder(savingGoalsUri(account)).GET().build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            return objectMapper.readValue(response.body(), SavingGoalList.class).getSavingsGoalList();
        } catch (InterruptedException | IOException e) {
            throw new ApiError("failed to fetch saving goals", e);
        }
    }

    @Override
    public NewSavingGoalResponse createSavingGoal(Account account, NewSavingGoalRequest savingGoalRequest) throws ApiError {
        try {
            var request = requestBuilder(newSavingGoalUri(account))
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(savingGoalRequest)))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            return objectMapper.readValue(response.body(), NewSavingGoalResponse.class);
        } catch (InterruptedException | IOException e) {
            throw new ApiError("failed to create saving goal", e);
        }
    }

    @Override
    public AddMoneyToSavingGoalResponse addMoneyToSavingGoal(Account account,SavingGoal savingGoal, Amount amount) throws ApiError {
        try {
            var addMoneyRequest = new AddMoneyToSavingGoalRequest();
            addMoneyRequest.setAmount(amount);

            var request = requestBuilder(addMoneyToSavingGoalUri(account, savingGoal.getSavingsGoalUid(), UUID.randomUUID()))
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(addMoneyRequest)))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            return objectMapper.readValue(response.body(), AddMoneyToSavingGoalResponse.class);
        } catch (InterruptedException | IOException e) {
           throw new ApiError("failed to add money to saving account", e);
        }
    }

    private HttpRequest.Builder requestBuilder(URI uri) {
        return HttpRequest.newBuilder()
                .uri(uri)
                .header("Accept", "application/json")
                .header("Authorization", String.format("Bearer %s", token));
    }

    private URI accountsUri() {
        return this.uri.resolve("accounts");
    }

    private URI transactionsUri(Account account, Instant since) {
        return this.uri.resolve(String.format("feed/account/%s/category/%s?changesSince=%s", account.getAccountUid(), account.getDefaultCategory(), format(since)));
    }

    private String format(Instant instant) {
        return DateTimeFormatter.ISO_INSTANT.format(instant);
    }

    private URI savingGoalsUri(Account account) {
        return this.uri.resolve(String.format("account/%s/savings-goals", account.getAccountUid()));
    }

    private URI addMoneyToSavingGoalUri(Account account, String savingGoalUid, UUID uuid) {
        return this.uri.resolve(String.format("account/%s/savings-goals/%s/add-money/%s", account.getAccountUid(), savingGoalUid, uuid));
    }

    private URI newSavingGoalUri(Account account) {
        return this.uri.resolve(String.format("account/%s/savings-goals", account.getAccountUid()));
    }

    private ObjectMapper buildObjectMapper() {
        var mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper;
    }

}
