package com.example.flightSearch.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Map;

@Service
public class AmadeusApiService {

    @Value("${amadeus.api.key}")
    private String amadeusApiKey;

    @Value("${amadeus.api.secret}")
    private String amadeusApiSecret;

    private final WebClient webClient;
    private final WebClient tokenWebClient;

    private String accessToken;
    private long tokenExpiry;

    public AmadeusApiService(WebClient.Builder webClientBuilder) {
        ExchangeStrategies strategies = ExchangeStrategies.builder()
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(1024 * 1024)) // 1MB
                .build();

        this.webClient = webClientBuilder.exchangeStrategies(strategies).build();
        this.tokenWebClient = webClientBuilder.build();
    }

    private Mono<String> getAccessToken() {
        if (accessToken != null && tokenExpiry > System.currentTimeMillis()) {
            return Mono.just(accessToken);
        }

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "client_credentials");
        formData.add("client_id", amadeusApiKey);
        formData.add("client_secret", amadeusApiSecret);

        return tokenWebClient.post()
                .uri("https://test.api.amadeus.com/v1/security/oauth2/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(formData))
                .retrieve()
                .bodyToMono(Map.class)
                .map(response -> {
                    accessToken = (String) response.get("access_token");
                    Integer expiresIn = (Integer) response.get("expires_in");
                    tokenExpiry = System.currentTimeMillis() + (expiresIn * 1000L) - 60000;
                    return accessToken;
                })
                .onErrorResume(e -> {
                    System.err.println("Error fetching token: " + e.getMessage());
                    return Mono.error(new RuntimeException("Failed to obtain Amadeus API token", e));
                });
    }


    public Mono<String> getFlightOffers(String originLocationCode, String destinationLocationCode, String departureDate, String returnDate, Integer adults, String currencyCode) {
        return getAccessToken()
                .flatMap(accessToken -> {
                    UriComponentsBuilder builder = UriComponentsBuilder.newInstance()
                            .scheme("https")
                            .host("test.api.amadeus.com")
                            .path("/v2/shopping/flight-offers")
                            .queryParam("originLocationCode", originLocationCode)
                            .queryParam("destinationLocationCode", destinationLocationCode)
                            .queryParam("departureDate", departureDate)
                            .queryParam("adults", adults)
                            .queryParam("max", 250);

                    if (!currencyCode.isEmpty()) {
                        builder.queryParam("currencyCode", currencyCode);
                    }

                    if (!returnDate.isEmpty()) {
                        builder.queryParam("returnDate", returnDate);
                    }

                    return webClient.get()
                            .uri(builder.build().toUriString())
                            .header("Authorization", "Bearer " + accessToken)
                            .retrieve()
                            .bodyToFlux(DataBuffer.class)
                            .reduce(DataBuffer::write)
                            .map(dataBuffer -> {
                                byte[] bytes = new byte[dataBuffer.readableByteCount()];
                                dataBuffer.read(bytes);
                                DataBufferUtils.release(dataBuffer);
                                return new String(bytes, StandardCharsets.UTF_8);
                            });
                })
                .onErrorResume(e -> {
                    System.err.println("Error fetching flight offers: " + e.getMessage());
                    return Mono.error(new RuntimeException("Failed to fetch flight offers", e));
                });
    }

}
