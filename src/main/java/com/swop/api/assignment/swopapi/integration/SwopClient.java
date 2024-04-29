package com.swop.api.assignment.swopapi.integration;

import com.swop.api.assignment.swopapi.dto.SwopApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;


@Component
public class SwopClient {

    @Qualifier("webClient_swop_api")
    private final WebClient swopApiClient;

    public SwopClient(WebClient swopApiClient) {
        this.swopApiClient = swopApiClient;
    }
    private final Logger logger = LoggerFactory.getLogger(SwopClient.class);
    private static final String CURRENCY_RATES_URL = "/rest/rates";

    public Flux<SwopApiResponse> fetchCurrencyRates() {
        return swopApiClient.get()
                .uri(CURRENCY_RATES_URL)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(SwopApiResponse.class)
                .cache()
                .onErrorResume(e -> {
                    logger.error("Something went wrong while fetching currency rates: {}", e.getMessage());
                    return Flux.empty();
                });
    }
}
