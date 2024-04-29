package com.swop.api.assignment.swopapi.controller;

import com.swop.api.assignment.swopapi.api.dto.ApiError;
import com.swop.api.assignment.swopapi.exception.CurrencyExchangeBadRequestException;
import com.swop.api.assignment.swopapi.exception.CurrencyExchangeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class DefaultExceptionHandler {
    @ExceptionHandler(value = {
            CurrencyExchangeBadRequestException.class
    })
    private ResponseEntity<Object> handleInvalidDataException(Exception ex) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage());
        log.warn("Bad request:{}", apiError);
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler(value = {
            CurrencyExchangeException.class
    })
    private ResponseEntity<Object> handleInternalServerErrorException(Exception ex) {
        ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, ex.getLocalizedMessage());
        log.error("Internal server error:{}", apiError);
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

}