package com.swop.api.assignment.swopapi.api;

import com.swop.api.assignment.swopapi.documentation.ExchangeRatesDoc;
import com.swop.api.assignment.swopapi.api.dto.CurrencyResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Mono;

@RequestMapping("api/currency")
public interface CurrencyEndpoint {

    @ExchangeRatesDoc
    @PostMapping("exchange")
    Mono<CurrencyResponse> currencyExchange(@RequestParam("source_currency") String baseCurrency,
                                            @RequestParam("target_currency") String targetCurrency,
                                            @RequestParam("amount") Double amount);

}
