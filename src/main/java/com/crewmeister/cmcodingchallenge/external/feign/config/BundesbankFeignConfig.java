package com.crewmeister.cmcodingchallenge.external.feign.config;

import feign.Retryer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for Feign client retry behavior when calling Bundesbank APIs.
 */
@Configuration
public class BundesbankFeignConfig {

    /**
     * Defines retry policy with initial interval, max interval, and max attempts.
     */
    @Bean
    public Retryer feignRetryer() {
        return new Retryer.Default(100, 1000, 5);
    }
}