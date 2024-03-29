package dev.rouchy.roundUp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import dev.rouchy.roundUp.clients.StarlingBankApiClientBuilder;
import dev.rouchy.roundUp.models.RoundUpRequest;
import dev.rouchy.roundUp.services.RoundUpServiceImpl;
import spark.Request;
import spark.Service;

import static spark.Service.ignite;

/*
                     ┌────┐
                     │User│
                     └────┘
                        │
                        │
                        │
                        │
                        │
                        │
                        ▼
┌────────────────────────────────────────────────────────────────────────────┐                         ┌───────────────────────────────────────────┐
│  Application (Spark framework endpoints)                                   │                         │                                           │
│                                                                            │                         │                                           │
│  - PUT /roundUp                                                            │                         │                                           │
│                                                                            │                         │                                           │
└────────────────────────┬───────────────────────────────────────────────────┘                         │                                           │
                         ▼                     ▲                                                       │                                           │
┌──────────────────────────────────────────────┴─────────────────────────────┐                         │                                           │
│  Services                                                                  │           ┌────────────▶│                                           │
│                                                                            │           │             │               Starling API                │
│     coordinate Starlings API calls                                         │           │             │                                           │
│     launch the round Up                                                    │           │             │                                           │
└──────────────────────┬───────────────────────────────┬─────────────────────┘           │             │                                           │
                       ▼  ▲                            ▼      ▲                          │             │                                           │
┌─────────────────────────┴───────────┐┌──────────────────────┴──────────────┐           │             │                                           │
│ Calculators                         ││  Clients                            │           │             │                                           │
│                                     ││                                     │           │             │                                           │
│   calculate the round UP amounts    ││  execute the call to the API        ├───────────┘             │                                           │
│   check the transaction eligibility ││                                     │                         │                                           │
└─────────────────────────────────────┘└─────────────────────────────────────┘                         └───────────────────────────────────────────┘
 */
public class Application {
    private final Service http;
    private final Environment environment;

    public Application(Environment environment, int port) {
        http = ignite().port(port);
        this.environment = environment;
    }

    public void start() {
        var mapper = mapper();

        http.get("/ping", (request, response) -> "pong");
        http.put("/transactions/roundUp", (request, response) -> {
            try {
                var token = extractToken(request);

                var service = new RoundUpServiceImpl(
                        StarlingBankApiClientBuilder.newBuilder().environment(this.environment).withToken(token).build()
                );


                var roundUpRequest = mapper.readValue(request.body(), RoundUpRequest.class);

                response.type("application/json");
                return mapper.writeValueAsString(service.roundUpCustomerTransactions(roundUpRequest));
            } catch (JsonProcessingException e) {
                this.http.halt(500);
                return "Server Error";
            }
        });
    }

    private ObjectMapper mapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper;
    }

    private String extractToken(Request request) {
        return request.headers("Authorization").replace("Bearer", "").trim();
    }

    public void stop() {
        http.stop();
    }

    public int port() {
        return http.port();
    }

    public static void main(String[] args) {
        new Application(Environment.SANDBOX, 8080).start();
    }

}
