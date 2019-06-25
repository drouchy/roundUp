package dev.rouchy.roundUp.integration;

import dev.rouchy.roundUp.Application;
import dev.rouchy.roundUp.Environment;
import dev.rouchy.roundUp.support.FakeStarling;
import dev.rouchy.roundUp.support.Fixtures;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RoundUpIntegrationTest {

    @Test
    public void canRoundUpTheTransactionsForTheCustomerAccounts() throws Exception {
        FakeStarling.ensureStarted();
        new Application(Environment.DEVELOPMENT, 9898).start();

        var client = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).build();

        var request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:9898/transactions/roundUp"))
                .header("Authorization", "Bearer VALID_TOKEN")
                .PUT(HttpRequest.BodyPublishers.ofString(Fixtures.load("roundup", "new")))
                .build();

        var response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertTrue(response.body().contains("93220ecc-2b09-4939-935b-6296ad212336"));
        assertTrue(response.body().contains("1836"));
        assertTrue(response.body().contains("GBP"));
    }

    @Test
    public void createsANewSavingGoalIfItDoesNotExist() throws Exception {
        FakeStarling.ensureStarted();
        new Application(Environment.DEVELOPMENT, 9899).start();

        var client = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).build();

        var request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:9899/transactions/roundUp"))
                .header("Authorization", "Bearer VALID_TOKEN")
                .PUT(HttpRequest.BodyPublishers.ofString(Fixtures.load("roundup", "new_not_existing")))
                .build();

        var response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertTrue(response.body().contains("93220ecc-2b09-4939-935b-3232432423"));
        assertTrue(response.body().contains("1836"));
        assertTrue(response.body().contains("GBP"));
    }
}
