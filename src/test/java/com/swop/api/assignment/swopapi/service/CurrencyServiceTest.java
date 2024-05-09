package com.swop.api.assignment.swopapi.service;

import com.swop.api.assignment.swopapi.api.dto.CurrencyResponse;
import com.swop.api.assignment.swopapi.dto.SwopApiResponse;
import com.swop.api.assignment.swopapi.exception.CurrencyExchangeBadRequestException;
import com.swop.api.assignment.swopapi.exception.CurrencyExchangeNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CurrencyServiceTest {

    @Mock
    private RedisService redisService;

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
        when(redisService.saveAndFetchRedisCache()).thenReturn(Flux.just(usdResponse, gbpResponse));

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
        when(redisService.saveAndFetchRedisCache()).thenReturn(Flux.just(usdResponse, eurResponse));

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
        when(redisService.saveAndFetchRedisCache()).thenReturn(Flux.just(usdResponse, eurResponse, gbpResponse));

        CurrencyResponse result = currencyService.exchangeCurrency("USD", "EUR", 100.0).block();

        assertNotNull(result);
        assertEquals(mockResponse.getSourceCurrency(), result.getSourceCurrency());
        assertEquals(mockResponse.getTargetCurrency(), result.getTargetCurrency());
        assertEquals(mockResponse.getMonetaryValue(), result.getMonetaryValue());
    }

    @Test
    void testCurrencyExchangeBadCurrencyInput() {
        String sourceCurrency = "eeee";
        String targetCurrency = "EUR";
        Double amount = 100.0;
        String errMsg = "Unable to find currencies with params: sourceCurrency: " + sourceCurrency + " and targetCurrency: " + targetCurrency;
        Exception exception = assertThrows(CurrencyExchangeBadRequestException.class, () -> {
            currencyService.exchange(sourceCurrency, targetCurrency, amount).block();
        });
        String exceptionMsg = exception.getMessage();
        assertTrue(errMsg.contains(exceptionMsg));
    }

    @Test
    void testCurrencyExchangeBadAmountInput() {
        String sourceCurrency = "USD";
        String targetCurrency = "EUR";
        Double amount = -100.0;
        String errMsg = "Invalid input data! Please check the value, it must be a positive number";
        Exception exception = assertThrows(CurrencyExchangeBadRequestException.class, () -> {
            currencyService.exchange(sourceCurrency, targetCurrency, amount).block();
        });
        String exceptionMsg = exception.getMessage();
        assertTrue(errMsg.contains(exceptionMsg));
    }

    @Test
    void testCurrencyExchangeNotFoundCurrency() {
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
        when(redisService.saveAndFetchRedisCache()).thenReturn(Flux.just(usdResponse, eurResponse));
        String sourceCurrency = "KYD";
        String targetCurrency = "EUR";
        Double amount = 100.0;
        String errMsg = "Not able to preform currency exchange due to unsupported currencies provided: source: " + sourceCurrency + " target: " + targetCurrency;
        Exception exception = assertThrows(CurrencyExchangeNotFoundException.class, () -> {
            currencyService.exchange(sourceCurrency, targetCurrency, amount).block();
        });
        String exceptionMsg = exception.getMessage();
        System.out.println(exceptionMsg);
        assertTrue(errMsg.contains(exceptionMsg));
    }

}
