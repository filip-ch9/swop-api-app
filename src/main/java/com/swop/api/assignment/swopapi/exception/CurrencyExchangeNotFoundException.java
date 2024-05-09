package com.swop.api.assignment.swopapi.exception;

import java.io.Serial;

public class CurrencyExchangeNotFoundException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -5374382370529355468L;

    public CurrencyExchangeNotFoundException(String message) {
        super(message);
    }
}
