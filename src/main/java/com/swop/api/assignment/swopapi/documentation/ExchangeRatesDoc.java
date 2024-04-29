package com.swop.api.assignment.swopapi.documentation;

import com.swop.api.assignment.swopapi.dto.CurrencyResponse;
import com.swop.api.assignment.swopapi.dto.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Operation(summary = "Currency conversion",
        description = "Converting an amount of money from one currency to another based on today's exchange rate")
@ApiResponses(value = {
        @ApiResponse(
                responseCode = "201",
                description = "Successful currency exchange",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = CurrencyResponse.class)
                )),
        @ApiResponse(responseCode = "400", description = "Bad request",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(
                        """     
                           {
                              "timestamp": "2024-04-28T14:16:34.618+00:00",
                              "path": "/api/currency/exchange",
                              "status": 400,
                              "error": "Bad Request",
                              "message": "Invalid Input, required parameter is missing!",
                              "requestId": "c0a5e5c0-9"      
                           }
                         """
                ))
        ),
        @ApiResponse(responseCode = "500", description = "Server Error",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = ErrorResponse.class),
                        examples = @ExampleObject(
                                """     
                                   {
                                      "timestamp": "2024-04-28T14:16:34.618+00:00",
                                      "path": "/api/currency/exchange",
                                      "status": 500,
                                      "error": "Internal Server Error",
                                      "message": "Invalid Currency Input: Base or target currency not found!",
                                      "requestId": "c0a5e5c0-9"      
                                   }
                                 """
                        )))
})
@CrossOrigin(origins = "http://localhost:63342")
@Component
public @interface ExchangeRatesDoc {
}
