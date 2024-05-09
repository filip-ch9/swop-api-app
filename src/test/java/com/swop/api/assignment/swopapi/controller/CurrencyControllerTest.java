package com.swop.api.assignment.swopapi.controller;

import com.swop.api.assignment.swopapi.api.dto.CurrencyResponse;
import com.swop.api.assignment.swopapi.service.CurrencyService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class CurrencyControllerTest {

    @Mock
    private CurrencyService currencyService;

    @InjectMocks
    private CurrencyController currencyController;

    @Test
    void testCurrencyExchangeController() {
        String sourceCurrency = "EUR";
        String targetCurrency = "USD";
        Double amount = 100.0;
        CurrencyResponse expectedResponse = new CurrencyResponse();

        Mockito.when(currencyService.exchange(sourceCurrency, targetCurrency, amount))
                .thenReturn(Mono.just(expectedResponse));

        Mono<CurrencyResponse> result = currencyController.currencyExchange(sourceCurrency, targetCurrency, amount);

        StepVerifier.create(result)
                .expectNext(expectedResponse)
                .verifyComplete();
    }

    @Test
    void testCurrencyExchangeControllerAmountNotDouble() {
        String sourceCurrency = "EUR";
        String targetCurrency = "USD";
        String amount = "invalidAmount";

        String errMsg = "For input string: \"invalidAmount\"";
        Exception exception = assertThrows(NumberFormatException.class, () -> {
            currencyController.currencyExchange(sourceCurrency, targetCurrency, Double.valueOf(amount)).block();
        });
        String exceptionMsg = exception.getMessage();
        System.out.println(exceptionMsg);
        assertTrue(errMsg.contains(exceptionMsg));
    }
}
