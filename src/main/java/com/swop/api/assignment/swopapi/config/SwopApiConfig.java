package com.swop.api.assignment.swopapi.config;

import io.micrometer.core.instrument.Clock;
import io.micrometer.core.ipc.http.OkHttpSender;
import io.micrometer.influx.InfluxConfig;
import io.micrometer.influx.InfluxMeterRegistry;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;


@Configuration
@EnableConfigurationProperties({WebProperties.class})
@RequiredArgsConstructor
public class SwopApiConfig {

    @Value("${management.influx.metrics.export.org}")
    private String org;

    @Value("${management.influx.metrics.export.bucket}")
    private String bucket;

    @Value("${management.influx.metrics.export.token}")
    private String token;

    private final WebProperties webProperties;

    @Bean(name = "webClient_swop_api")
    public WebClient swopApiClient(WebClient.Builder webClient) {
        return webClient.baseUrl(webProperties.getSwopBaseUrl())
                .defaultHeader("Authorization", webProperties.getApiKey())
                .build();
    }

    @Bean
    public InfluxMeterRegistry influxMeterRegistry(InfluxConfig influxConfig, Clock clock) {

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        httpClient.addInterceptor(chain -> {
            Request original = chain.request();

            if (!original.url().pathSegments().contains("write")) {
                return  chain.proceed(original);
            }

            HttpUrl url = original.url()
                    .newBuilder()
                    .removePathSegment(0)
                    .addEncodedPathSegments("api/v2/write")
                    .removeAllQueryParameters("db")
                    .removeAllQueryParameters("consistency")
                    .addQueryParameter("org", org)
                    .addQueryParameter("bucket", bucket)
                    .build();

            Request request = original.newBuilder()
                    .url(url)
                    .header("Authorization", "Token " + token)
                    .build();

            return chain.proceed(request);
        });

        return InfluxMeterRegistry.builder(influxConfig)
                .clock(clock)
                .httpClient(new OkHttpSender(httpClient.build()))
                .build();
    }

    @Bean
    InfluxConfig influxConfig() {
        return new InfluxConfig() {
            @Override
            public String get(@NotNull String key) {
                return null;
            }

            @NotNull
            @Override
            public Duration step() {
                return Duration.ofSeconds(20);
            }

            @Override
            public String org() {
                return org;
            }

            @NotNull
            @Override
            public String bucket() {
                return bucket;
            }

            @Override
            public String token() {
                return token;
            }
        };
    }
}
