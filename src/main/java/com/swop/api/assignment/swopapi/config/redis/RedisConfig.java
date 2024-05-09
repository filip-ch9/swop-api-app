package com.swop.api.assignment.swopapi.config.redis;

import com.swop.api.assignment.swopapi.exception.RedisUriException;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.resource.DefaultClientResources;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URI;
import java.net.URISyntaxException;

@Configuration
@EnableConfigurationProperties(RedisProperties.class)
public class RedisConfig {

    private RedisURI getRedisURI(String url) {
        URI parsedRedisUri;
        try {
            parsedRedisUri = new URI(url);
        } catch (URISyntaxException e) {
            throw new RedisUriException("Failed to parse redis URI to URI object: " + url, e);
        }

        String[] userInfo = (parsedRedisUri.getUserInfo() == null ? "" : parsedRedisUri.getUserInfo()).split(":");
        String redisPassword;
        String redisUsername;
        if (userInfo.length == 1) {
            redisUsername = "";
            redisPassword = userInfo[0];
        } else {
            redisUsername = userInfo[0];
            redisPassword = userInfo[1];
        }

        return RedisURI.Builder.redis(parsedRedisUri.getHost(), parsedRedisUri.getPort())
                .withSsl("rediss".equals(parsedRedisUri.getScheme()))
                .withVerifyPeer(!"rediss".equals(parsedRedisUri.getScheme()))
                .withClientName(redisUsername)
                .withPassword(redisPassword.toCharArray())
                .build();
    }

    @Bean("redis-client")
    public RedisClient redisClient(RedisProperties redisProperties) {
        return RedisClient.create(
                DefaultClientResources.create(),
                getRedisURI(redisProperties.getUrl()));
    }

}
