package com.swop.api.assignment.swopapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ErrorResponse {
    private String timeStamp;
    private String path;
    private Integer status;
    private String error;
    private String message;
    private String requestId;
}
