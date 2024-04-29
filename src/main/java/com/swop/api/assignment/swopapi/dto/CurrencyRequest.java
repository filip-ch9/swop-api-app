package com.swop.api.assignment.swopapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CurrencyRequest {
    @NotBlank
    @Schema(type = "string", example = "EUR")
    private String sourceCurrency;
    @NotBlank
    @Schema(type = "string", example = "USD")
    private String targetCurrency;
    @NotBlank
    @Schema(type = "double", example = "100")
    private Double amount;
}
