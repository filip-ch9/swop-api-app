package com.swop.api.assignment.swopapi.config.influxdb;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "influx")
public class InfluxProperties {
    private String bucket;
    private String token;
    private String org;
    private String uri;
    private String userName;
    private String password;

}
