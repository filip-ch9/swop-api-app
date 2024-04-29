package com.swop.api.assignment.swopapi.documentation;

import com.swop.api.assignment.swopapi.api.dto.CurrencyResponse;
import com.swop.api.assignment.swopapi.api.dto.ApiError;
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
                schema = @Schema(implementation = ApiError.class),
                examples = @ExampleObject(
                        """     
                           {
                              "status": BAD REQUEST,
                              "message": "Invalid Input, required parameter is missing!"    
                           }
                         """
                ))
        ),
        @ApiResponse(responseCode = "500", description = "Server Error",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = ApiError.class),
                        examples = @ExampleObject(
                                """     
                                   {
                                      "status": "INTERNAL_SERVER_ERROR",
                                      "message": "Localised error message"
                                   }
                                 """
                        )))
})
@CrossOrigin(origins = "http://localhost:63342")
@Component
public @interface ExchangeRatesDoc {
}
