package com.swop.api.assignment.swopapi.exception;

import java.io.Serial;

public class RedisUriException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 5344774722523906706L;

    public RedisUriException(String message, Throwable cause) {
        super(message, cause);
    }
}
