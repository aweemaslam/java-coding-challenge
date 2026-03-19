package com.crewmeister.cmcodingchallenge.external.adapter;

import com.crewmeister.cmcodingchallenge.external.feign.BundesbankFeignClient;
import com.crewmeister.cmcodingchallenge.external.feign.response.ExchangeRateApiResponse;
import com.crewmeister.cmcodingchallenge.external.port.ExchangeRateProviderPort;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExchangeRateProviderAdapter implements ExchangeRateProviderPort {

    public static final String DETAIL_TYPE = "dataonly";
    private final BundesbankFeignClient bundesbankFeignClient;

    @Cacheable("exchange-rates")
    @CircuitBreaker(name = "exchangeRate", fallbackMethod = "fallbackAllRates")
    @Override
    public ExchangeRateApiResponse getRates(LocalDate date) {
        if (date == null) {
            return bundesbankFeignClient.getAllExchangeRates(DETAIL_TYPE);
        } else {
            return bundesbankFeignClient.getExchangeRatesByDate(date.toString(), date.toString(), DETAIL_TYPE);
        }

    }

    public ExchangeRateApiResponse fallbackAllRates(Throwable t) {
        // Could return cached values or empty list
        return null;

    }
}