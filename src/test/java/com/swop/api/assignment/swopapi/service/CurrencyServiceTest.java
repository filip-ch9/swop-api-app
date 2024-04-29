package com.swop.api.assignment.swopapi.service;

import com.swop.api.assignment.swopapi.api.dto.CurrencyResponse;
import com.swop.api.assignment.swopapi.dto.SwopApiResponse;
import com.swop.api.assignment.swopapi.exception.CurrencyExchangeException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CurrencyServiceTest {

    @Mock
    private Function<String, Mono<List<SwopApiResponse>>> swopCache;

    @InjectMocks
    private CurrencyService currencyService;

    @Test
    void testCurrencyExchange() {
        SwopApiResponse usdResponse = SwopApiResponse.builder()
                .baseCurrency("EUR")
                .quoteCurrency("USD")
                .quote(1.07)
                .build();
        SwopApiResponse gbpResponse = SwopApiResponse.builder()
                .baseCurrency("EUR")
                .quoteCurrency("GBP")
                .quote(0.85)
                .build();
        CurrencyResponse mockResponse = CurrencyResponse.builder()
                .sourceCurrency("USD")
                .targetCurrency("GBP")
                .monetaryValue("79.44 £").build();
        when(swopCache.apply("currencies")).thenReturn(Mono.just(List.of(usdResponse, gbpResponse)));

        CurrencyResponse result = currencyService.exchangeCurrency("USD", "GBP", 100.0).block();

        assertNotNull(result);
        assertEquals(mockResponse.getSourceCurrency(), result.getSourceCurrency());
        assertEquals(mockResponse.getTargetCurrency(), result.getTargetCurrency());
        assertEquals(mockResponse.getMonetaryValue(), result.getMonetaryValue());
    }

    @Test
    void testCurrencyExchangeEurToEur() {
        SwopApiResponse usdResponse = SwopApiResponse.builder()
                .baseCurrency("EUR")
                .quoteCurrency("USD")
                .quote(1.07)
                .build();
        SwopApiResponse eurResponse = SwopApiResponse.builder()
                .baseCurrency("EUR")
                .quoteCurrency("EUR")
                .quote(1.0)
                .build();
        CurrencyResponse mockResponse = CurrencyResponse.builder()
                .sourceCurrency("EUR")
                .targetCurrency("EUR")
                .monetaryValue("100 €").build();
        when(swopCache.apply("currencies")).thenReturn(Mono.just(List.of(usdResponse, eurResponse)));

        CurrencyResponse result = currencyService.exchangeCurrency("EUR", "EUR", 100.0).block();

        assertNotNull(result);
        assertEquals(mockResponse.getSourceCurrency(), result.getSourceCurrency());
        assertEquals(mockResponse.getTargetCurrency(), result.getTargetCurrency());
        assertEquals(mockResponse.getMonetaryValue(), result.getMonetaryValue());
    }

    @Test
    void testCurrencyExchangeEurAsTarget() {
        SwopApiResponse usdResponse = SwopApiResponse.builder()
                .baseCurrency("EUR")
                .quoteCurrency("USD")
                .quote(1.07)
                .build();
        SwopApiResponse eurResponse = SwopApiResponse.builder()
                .baseCurrency("EUR")
                .quoteCurrency("EUR")
                .quote(1.0)
                .build();
        SwopApiResponse gbpResponse = SwopApiResponse.builder()
                .baseCurrency("EUR")
                .quoteCurrency("GBP")
                .quote(0.85)
                .build();
        CurrencyResponse mockResponse = CurrencyResponse.builder()
                .sourceCurrency("USD")
                .targetCurrency("EUR")
                .monetaryValue("93.46 €").build();
        when(swopCache.apply("currencies")).thenReturn(Mono.just(List.of(usdResponse, gbpResponse, eurResponse)));

        CurrencyResponse result = currencyService.exchangeCurrency("USD", "EUR", 100.0).block();

        assertNotNull(result);
        assertEquals(mockResponse.getSourceCurrency(), result.getSourceCurrency());
        assertEquals(mockResponse.getTargetCurrency(), result.getTargetCurrency());
        assertEquals(mockResponse.getMonetaryValue(), result.getMonetaryValue());
    }

    @Test
    void testCurrencyExchange_Error() {
        when(swopCache.apply("currencies")).thenReturn(Mono.just(Collections.emptyList())); // Mocking empty list
        String sourceCurrency = "USD";
        String targetCurrency = "EUR";
        Double amount = 100.0;

        Mono<CurrencyResponse> resultMono = currencyService.exchangeCurrency(sourceCurrency, targetCurrency, amount);

        StepVerifier.create(resultMono)
                .expectError(CurrencyExchangeException.class)
                .verify();
    }


}
