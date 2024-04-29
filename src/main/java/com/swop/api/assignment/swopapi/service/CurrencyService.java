package com.swop.api.assignment.swopapi.service;

import com.swop.api.assignment.swopapi.dto.CurrencyResponse;
import com.swop.api.assignment.swopapi.dto.SwopApiResponse;
import com.swop.api.assignment.swopapi.exception.CurrencyExchangeException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.List;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class CurrencyService {

    private final Function<String, Mono<List<SwopApiResponse>>> swopCache;

    private final Logger logger = LoggerFactory.getLogger(CurrencyService.class);

    public Mono<CurrencyResponse> exchangeCurrency(String baseCurrency, String targetCurrency, Double amount) {
        return getCurrencyCache()
                .collectList()
                .flatMap(currencyList -> {
                    if (currencyList.stream().noneMatch(v -> validateInput(v, baseCurrency, targetCurrency))) {
                        return Mono.error(new CurrencyExchangeException("Invalid Currency Input: Base or target currency not found!"));
                    }
                    if ("EUR".equals(baseCurrency)) {
                        return calculateCurrencyExchange(baseCurrency, targetCurrency, amount, true);
                    } else {
                        return calculateCurrencyExchange(targetCurrency, baseCurrency, amount, false);
                    }
                });
    }

    private Mono<CurrencyResponse> calculateCurrencyExchange(String currency,
                                                             String exchangeCurrency,
                                                             Double amount,
                                                             boolean shouldMultiply) {
        return Mono.from(getCurrencyCache()
                        .filter(swopApiResponse -> swopApiResponse.getBaseCurrency().equals(currency)
                                && swopApiResponse.getQuoteCurrency().equals(exchangeCurrency)
                        )
                        .map(swopApiResponse -> shouldMultiply ?
                                amount * swopApiResponse.getQuote() : amount / swopApiResponse.getQuote())
                        .map(exchangeResult -> buildResponse(shouldMultiply, exchangeResult, currency, exchangeCurrency))
                )
                .onErrorMap(ex -> {
                    logger.error("Unable to perform currency exchange with params: currency:{}, exchange currency:{}, amount:{}, with error message: {}",
                            currency, exchangeCurrency, amount, ex.getMessage());
                    throw new CurrencyExchangeException("Something went wrong during currency conversion");
                });
    }

    public CurrencyResponse buildResponse(boolean isMultiply,
                                          Double exchangeResult,
                                          String baseCurrency,
                                          String targetCurrency) {

        if (isMultiply) {
            var currencyResponseBuilder = CurrencyResponse.builder()
                    .monetaryValue(formatMonetaryValue(exchangeResult, targetCurrency));
            return currencyResponseBuilder.sourceCurrency(baseCurrency)
                    .targetCurrency(targetCurrency)
                    .build();
        }
        var currencyResponseBuilder = CurrencyResponse.builder()
                .monetaryValue(formatMonetaryValue(exchangeResult, baseCurrency));
        return currencyResponseBuilder
                .sourceCurrency(targetCurrency)
                .targetCurrency(baseCurrency)
                .build();
    }

    private String formatMonetaryValue(Double exchangeResult, String targetCurrency) {
        Currency currency = Currency.getInstance(targetCurrency);
        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMaximumFractionDigits(2);
        return nf.format(exchangeResult) + " " + currency.getSymbol();
    }

    public Flux<SwopApiResponse> getCurrencyCache() {
        return swopCache.apply("currencies").flatMapMany(Flux::fromIterable);
    }

    private boolean validateInput(SwopApiResponse swopApiResponse, String baseCurrency, String targetCurrency) {
        return (swopApiResponse.getBaseCurrency().equals(baseCurrency) && swopApiResponse.getQuoteCurrency().equals(targetCurrency)) ||
                (swopApiResponse.getBaseCurrency().equals(targetCurrency) && swopApiResponse.getQuoteCurrency().equals(baseCurrency));
    }

}
