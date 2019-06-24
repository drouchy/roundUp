package dev.rouchy.roundUp.clients;

import dev.rouchy.roundUp.Environment;
import org.junit.jupiter.api.Test;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertEquals;


class StarlingBankApiClientBuilderTest {

    @Test
    public void byDefaultBuildAProductionClient() {
        StarlingBankApiClient client = StarlingBankApiClientBuilder.newBuilder().build();

        assertEquals(URI.create("https://api.starlingbank.com:443/api/v2/"), client.baseUri());
    }

    @Test
    public void buildsAProductionClient() {
        StarlingBankApiClient client = StarlingBankApiClientBuilder.newBuilder().environment(Environment.PRODUCTION).build();

        assertEquals(URI.create("https://api.starlingbank.com:443/api/v2/"), client.baseUri());
    }

    @Test
    public void buildsASandboxClient() {
        StarlingBankApiClient client = StarlingBankApiClientBuilder.newBuilder().environment(Environment.SANDBOX).build();

        assertEquals(URI.create("https://api-sandbox.starlingbank.com:443/api/v2/"), client.baseUri());
    }

    @Test
    public void buildsADevelopmentClient() {
        StarlingBankApiClient client = StarlingBankApiClientBuilder.newBuilder().environment(Environment.DEVELOPMENT).build();

        assertEquals(URI.create("http://localhost:8888/api/v2/"), client.baseUri());
    }

    @Test
    public void buildsACustomClient() {
        StarlingBankApiClient client = StarlingBankApiClientBuilder.newBuilder()
                .withScheme("ftp").withHostname("starling.example.com").withPort(21).build();

        assertEquals(URI.create("ftp://starling.example.com:21/api/v2/"), client.baseUri());
    }

}