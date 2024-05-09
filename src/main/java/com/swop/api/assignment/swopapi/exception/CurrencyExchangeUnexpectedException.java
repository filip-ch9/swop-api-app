package com.swop.api.assignment.swopapi.exception;

import java.io.Serial;

public class CurrencyExchangeUnexpectedException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 6730316598820272014L;

    public CurrencyExchangeUnexpectedException(String message) {
        super(message);
    }
}
