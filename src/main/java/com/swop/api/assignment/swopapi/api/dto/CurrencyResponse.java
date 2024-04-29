package com.swop.api.assignment.swopapi.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CurrencyResponse {
    @Schema(type = "string", example = "EUR")
    private String sourceCurrency;
    @Schema(type = "string", example = "USD")
    private String targetCurrency;
    @Schema(type = "string", example = "100 US$")
    private String monetaryValue;
}
