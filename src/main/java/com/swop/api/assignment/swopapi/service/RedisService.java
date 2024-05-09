package com.swop.api.assignment.swopapi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.swop.api.assignment.swopapi.dto.SwopApiResponse;
import com.swop.api.assignment.swopapi.exception.RedisReadWriteException;
import com.swop.api.assignment.swopapi.integration.SwopClient;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;

@Service
public class RedisService {

    @Qualifier("redis-client")
    private final RedisClient redisClient;
    private final SwopClient swopClient;
    private final Logger logger = LoggerFactory.getLogger(RedisService.class);
    private final ObjectMapper objectMapper;
    private static final String REDIS_KEY = "currencies";

    public RedisService(RedisClient redisClient,
                        SwopClient swopClient,
                        ObjectMapper objectMapper) {
        this.redisClient = redisClient;
        this.swopClient = swopClient;
        this.objectMapper = objectMapper;
    }

    // TODO improve this code block
    public Flux<Void> saveCache(List<SwopApiResponse> swopApiResponseList) {
        return Flux.defer(() -> {
            try (StatefulRedisConnection<String, String> connection = redisClient.connect()) {
                RedisCommands<String, String> commands = connection.sync();
                if (commands.get(REDIS_KEY) == null) {
                    String jsonString = objectMapper.writeValueAsString(swopApiResponseList);
                    long expirationInMs = 24 * 60 * 60 * 1000;
                    connection.async().psetex(REDIS_KEY, expirationInMs, jsonString);
                    logger.info("Swop Api Response list has been cached to redis!");
                } else {
                    logger.info("Swop Api Response list already exists in redis cache. Skipping caching.");
                }
                return Flux.empty();
            } catch (Exception e) {
                logger.error("Unable to parse swop api response list into json string and save to redis!");
                return Flux.error(e);
            }
        });
    }

    public Flux<SwopApiResponse> getRedisCache(String key) {
        return Flux.using(
                redisClient::connect,
                connection -> connection.reactive().get(key)
                        .flatMapMany(response -> {
                            try {
                                List<SwopApiResponse> swopApiResponseList = objectMapper.readValue(response, new TypeReference<>() {});
                                return Flux.fromIterable(swopApiResponseList);
                            } catch (JsonProcessingException e) {
                                logger.error("Unable to parse response from redis: {}", response);
                                return Flux.error(new RedisReadWriteException("Something went wrong while parsing redis cache: " + e.getMessage()));
                            }
                        }),
                StatefulRedisConnection::close
        ).cache();
    }

    public Flux<SwopApiResponse> saveAndFetchRedisCache() {
        return getRedisCache(REDIS_KEY)
                .switchIfEmpty(
                        swopClient.fetchCurrencyRates()
                                .collectList()
                                .flatMapMany(this::saveCache)
                                .thenMany(getRedisCache(REDIS_KEY))
                );
    }


}
