package com.swop.api.assignment.swopapi.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@EnableConfigurationProperties({WebProperties.class})
@RequiredArgsConstructor
public class SwopApiConfig {

    private final WebProperties webProperties;

    @Bean(name = "webClient_swop_api")
    public WebClient swopApiClient(WebClient.Builder webClient) {
        return webClient.baseUrl(webProperties.getSwopBaseUrl())
                .defaultHeader("Authorization", webProperties.getApiKey())
                .build();
    }
}
