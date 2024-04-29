package com.swop.api.assignment.swopapi.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "web")
public class WebProperties {
    private String swopBaseUrl;
    private String apiKey;
}
