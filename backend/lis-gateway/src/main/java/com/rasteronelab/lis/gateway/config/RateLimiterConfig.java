package com.rasteronelab.lis.gateway.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import reactor.core.publisher.Mono;

/**
 * Rate limiter key resolvers for the Spring Cloud Gateway.
 * Used with the built-in {@code RequestRateLimiter} filter backed by Redis.
 */
@Configuration
public class RateLimiterConfig {

    /**
     * Resolves rate-limiting key by client IP address.
     * Used for general API rate limiting and login endpoint throttling.
     */
    @Bean
    public KeyResolver ipKeyResolver() {
        return exchange -> {
            var remoteAddress = exchange.getRequest().getRemoteAddress();
            String key = (remoteAddress != null)
                    ? remoteAddress.getAddress().getHostAddress()
                    : "unknown";
            return Mono.just(key);
        };
    }
}
