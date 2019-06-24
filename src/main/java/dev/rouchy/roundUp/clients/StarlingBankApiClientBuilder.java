package dev.rouchy.roundUp.clients;

import dev.rouchy.roundUp.Environment;

import java.net.URI;

public class StarlingBankApiClientBuilder {
    private String scheme = "https";
    private String hostname = "api.starlingbank.com";
    private String apiVersion = "v2";
    private Integer port = 443;
    private String token;

    public static StarlingBankApiClientBuilder newBuilder() {
        return new StarlingBankApiClientBuilder();
    }

    public StarlingBankApiClient build() {
        return new StarlingBankApiClient(buildUrl(), this.token);
    }

    public StarlingBankApiClientBuilder withScheme(String scheme) {
        this.scheme = scheme;
        return this;
    }

    public StarlingBankApiClientBuilder withHostname(String hostname) {
        this.hostname = hostname;
        return this;
    }

    public StarlingBankApiClientBuilder withPort(Integer port) {
        this.port = port;
        return this;
    }

    public StarlingBankApiClientBuilder withToken(String token) {
        this.token = token;
        return this;
    }

    private URI buildUrl() {
        return URI.create(String.format("%s://%s:%d/api/%s/", this.scheme, this.hostname, this.port, this.apiVersion));
    }

    public StarlingBankApiClientBuilder environment(Environment environment) {
        switch (environment) {
            case PRODUCTION:
                return this.withScheme("https").withHostname("api.starlingbank.com").withPort(443);
            case SANDBOX:
                return this.withScheme("https").withHostname("api-sandbox.starlingbank.com").withPort(443);
            case DEVELOPMENT:
                return this.withScheme("http").withHostname("localhost").withToken("VALID_TOKEN").withPort(8888);
            default: throw new RuntimeException("Unsupported environment "+ environment);
        }
    }
}
