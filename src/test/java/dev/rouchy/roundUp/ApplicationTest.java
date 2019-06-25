package dev.rouchy.roundUp;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ApplicationTest {
    private static final Random RANDOM = new Random();

    private Application application;

    @BeforeEach
    public void setUp() {
        application = new Application(Environment.DEVELOPMENT, 8787);
        application.start();
    }

    @AfterEach
    public void tearDown() {
        application.stop();
    }

    @Test
    public void startsAHttpServerWithAPingEndpoint() throws Exception {
        var client = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).build();

        var request = HttpRequest.newBuilder()
                .uri(URI.create(String.format("http://localhost:%d/ping", application.port())))
                .GET()
                .build();

        var response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals("pong", response.body());
    }
}