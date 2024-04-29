package com.swop.api.assignment.swopapi.exception;

import java.io.Serial;

public class CurrencyExchangeBadRequestException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 6402205968921967380L;

    public CurrencyExchangeBadRequestException(String message) {
        super(message);
    }
}
