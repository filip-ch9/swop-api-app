package com.swop.api.assignment.swopapi.api;

import com.swop.api.assignment.swopapi.documentation.ExchangeRatesDoc;
import com.swop.api.assignment.swopapi.dto.CurrencyResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Mono;

@RequestMapping("api/currency")
public interface CurrencyEndpoint {

    @ExchangeRatesDoc
    @GetMapping("exchange")
    Mono<CurrencyResponse> currencyExchange(@RequestParam("base_currency") String baseCurrency,
                                            @RequestParam("target_currency") String targetCurrency,
                                            @RequestParam("amount") Double amount);

}
