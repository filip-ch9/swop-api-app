package com.swop.api.assignment.swopapi.controller;

import com.swop.api.assignment.swopapi.api.CurrencyEndpoint;
import com.swop.api.assignment.swopapi.api.dto.CurrencyResponse;
import com.swop.api.assignment.swopapi.service.CurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class CurrencyController implements CurrencyEndpoint {

    private final CurrencyService currencyService;

    @Override
    public Mono<CurrencyResponse> currencyExchange(String sourceCurrency, String targetCurrency, Double amount) {
        return currencyService.exchange(sourceCurrency, targetCurrency, amount);
    }

}
