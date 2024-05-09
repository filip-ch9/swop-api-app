package com.swop.api.assignment.swopapi.exception;

import java.io.Serial;

public class RedisReadWriteException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -1623700746839884215L;

    public RedisReadWriteException(String message) {
        super(message);
    }
}
