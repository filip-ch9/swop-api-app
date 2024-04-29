package com.swop.api.assignment.swopapi.service;

import com.swop.api.assignment.swopapi.dto.CurrencyResponse;
import com.swop.api.assignment.swopapi.dto.SwopApiResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

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
        SwopApiResponse swopApiResponse = SwopApiResponse.builder()
                .baseCurrency("EUR")
                .quoteCurrency("USD")
                .quote(1.07)
                .build();
        CurrencyResponse mockResponse = new CurrencyResponse();
        mockResponse.setSourceCurrency("USD");
        mockResponse.setTargetCurrency("EUR");
        mockResponse.setMonetaryValue("93.46 €");

        when(swopCache.apply("currencies")).thenReturn(Mono.just(java.util.List.of(swopApiResponse)));

        CurrencyResponse result = currencyService.exchangeCurrency("USD", "EUR", 100.0).block();

        assertNotNull(result);
        assertEquals(mockResponse.getSourceCurrency(), result.getSourceCurrency());
        assertEquals(mockResponse.getTargetCurrency(), result.getTargetCurrency());
        assertEquals(mockResponse.getMonetaryValue(), result.getMonetaryValue());
    }

}
