package com.swop.api.assignment.swopapi.cache;

import com.swop.api.assignment.swopapi.dto.SwopApiResponse;
import com.swop.api.assignment.swopapi.integration.SwopClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.function.Function;

import static com.swop.api.assignment.swopapi.cache.CustomCacheManager.ofMono;

@Configuration
public class CacheConfig {
    private static final Long EXPIRATION_DAYS = 1L;

    @Bean
    public Function<String, Mono<List<SwopApiResponse>>> swopCurrenciesCache(SwopClient swopClient) {
        return ofMono(Duration.ofDays(EXPIRATION_DAYS), s -> swopClient.fetchCurrencyRates().collectList());
    }

}
