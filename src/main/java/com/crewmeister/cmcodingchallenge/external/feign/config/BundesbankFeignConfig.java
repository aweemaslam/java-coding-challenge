package com.crewmeister.cmcodingchallenge.external.feign.config;

import feign.Retryer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BundesbankFeignConfig {
    @Bean
    public Retryer feignRetryer() {
        // Retryer.Default(period, maxPeriod, maxAttempts)
        // period = 100ms, maxPeriod = 1s, maxAttempts = 5
        return new Retryer.Default(100, 1000, 5);
    }
}

