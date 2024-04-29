package com.swop.api.assignment.swopapi.api.dto;

import lombok.Value;
import org.springframework.http.HttpStatus;

@Value
public class ApiError {

    HttpStatus status;
    String message;

    public ApiError(HttpStatus status, String message) {
        super();
        this.status = status;
        this.message = message;
    }

    public ApiError(HttpStatus status, String message, String error) {
        super();
        this.status = status;
        this.message = message;
    }
}