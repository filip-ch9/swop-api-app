package com.swop.api.assignment.swopapi.service;

import com.swop.api.assignment.swopapi.api.dto.CurrencyResponse;
import com.swop.api.assignment.swopapi.dto.SwopApiResponse;
import com.swop.api.assignment.swopapi.exception.CurrencyExchangeBadRequestException;
import com.swop.api.assignment.swopapi.exception.CurrencyExchangeException;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.List;
import java.util.function.Function;

import static com.swop.api.assignment.swopapi.util.ValidationUtil.isValidCurrency;
import static com.swop.api.assignment.swopapi.util.ValidationUtil.validate;

@Service
@RequiredArgsConstructor
public class CurrencyService {

    private final Function<String, Mono<List<SwopApiResponse>>> swopCache;
    private static final Logger logger = LoggerFactory.getLogger(CurrencyService.class);

    public Mono<CurrencyResponse> exchange(String sourceCurrency,
                                           String targetCurrency,
                                           Double amount) {
        isValidCurrency(sourceCurrency, targetCurrency);
        if (amount < 0) {
            return Mono.error(new CurrencyExchangeBadRequestException("Invalid input data! Please check the value, it must be a positive number"));
        }
        return exchangeCurrency(sourceCurrency, targetCurrency, amount);
    }

    public Mono<CurrencyResponse> exchangeCurrency(String sourceCurrency,
                                                   String targetCurrency,
                                                   Double amount) {
        return getCurrencyCache()
                .collectList()
                .flatMap(currencyList -> {
                    if (validate(currencyList, targetCurrency)) {
                        return calculateCurrencyExchange(sourceCurrency, targetCurrency, amount, currencyList);
                    }
                    logger.error("Unable to perform currency exchange with params: currency:{}, target currency:{}, amount:{}",
                            sourceCurrency, targetCurrency, amount);
                    return Mono.error(new CurrencyExchangeException("Something went wrong while calculating exchange"));
                });
    }


    public Mono<CurrencyResponse> calculateCurrencyExchange(String sourceCurrency,
                                                            String targetCurrency,
                                                            Double amount,
                                                            List<SwopApiResponse> currencyList) {
        Double sourceCurrencyValue = getQuote(sourceCurrency, currencyList);
        Double targetCurrencyValue = getQuote(targetCurrency, currencyList);
        if (sourceCurrencyValue != null && targetCurrencyValue != null && sourceCurrencyValue != 0) {
            double rate = targetCurrencyValue / sourceCurrencyValue;
            return Mono.just(CurrencyResponse.builder()
                    .sourceCurrency(sourceCurrency)
                    .targetCurrency(targetCurrency)
                    .monetaryValue(formatMonetaryValue(amount * rate, targetCurrency))
                    .build());
        } else {
            return Mono.error(new CurrencyExchangeException("Error occurred while calculating exchange rate"));
        }
    }

    @Nullable
    private static Double getQuote(String sourceCurrency, List<SwopApiResponse> currencyList) {
        return currencyList.stream()
                .filter(v -> "EUR".equals(v.getBaseCurrency()) && v.getQuoteCurrency().equals(sourceCurrency))
                .findFirst()
                .map(SwopApiResponse::getQuote)
                .orElse(null);
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

}
