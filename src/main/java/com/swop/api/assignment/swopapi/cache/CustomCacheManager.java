package com.swop.api.assignment.swopapi.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.experimental.UtilityClass;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.function.Function;

@UtilityClass
public class CustomCacheManager {

    public static <T> Function<String, Mono<T>> ofMono(Duration duration, Function<String, Mono<T>> fn) {
        Cache<String, T> cache = Caffeine.newBuilder()
                .expireAfterWrite(duration)
                .recordStats()
                .build();
        return key -> {
            T result = cache.getIfPresent(key);
            if (result != null) {
                return Mono.just(result);
            } else {
                return fn.apply(key).doOnNext(n -> cache.put(key, n));
            }
        };
    }
}
