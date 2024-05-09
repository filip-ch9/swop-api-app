package com.swop.api.assignment.swopapi.util;

import com.swop.api.assignment.swopapi.dto.SwopApiResponse;
import com.swop.api.assignment.swopapi.exception.CurrencyExchangeBadRequestException;
import lombok.experimental.UtilityClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Currency;
import java.util.List;

@UtilityClass
public class ValidationUtil {
    private static final Logger logger = LoggerFactory.getLogger(ValidationUtil.class);
    public static boolean isValidCurrency(String sourceCurrency, String targetCurrency) {
        try {
            return Currency.getAvailableCurrencies().contains(Currency.getInstance(sourceCurrency)) &&
                    Currency.getAvailableCurrencies().contains(Currency.getInstance(targetCurrency));
        } catch (IllegalArgumentException e) {
            String errMsg = "Unable to find currencies with params: sourceCurrency: " + sourceCurrency + " and targetCurrency: " + targetCurrency;
            logger.error("Error message: {}, cause: {}", errMsg, e.getMessage());
            throw new CurrencyExchangeBadRequestException(errMsg);
        }
    }

    public static boolean validate(List<SwopApiResponse> swopApiResponses, String targetCurrency, String sourceCurrency) {
        return swopApiResponses.stream()
                .anyMatch(response ->
                        (response.getBaseCurrency().equals("EUR") && response.getQuoteCurrency().equals(targetCurrency)) ||
                                (response.getBaseCurrency().equals(targetCurrency) && response.getQuoteCurrency().equals("EUR"))) &&
                swopApiResponses.stream()
                        .anyMatch(response ->
                                (response.getBaseCurrency().equals("EUR") && response.getQuoteCurrency().equals(sourceCurrency)) ||
                                        (response.getBaseCurrency().equals(sourceCurrency) && response.getQuoteCurrency().equals("EUR")));
    }
}
