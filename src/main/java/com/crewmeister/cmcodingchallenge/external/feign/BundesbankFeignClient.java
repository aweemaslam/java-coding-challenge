package com.crewmeister.cmcodingchallenge.external.feign;

import com.crewmeister.cmcodingchallenge.external.feign.config.BundesbankFeignConfig;
import com.crewmeister.cmcodingchallenge.external.feign.response.ExchangeRateApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Feign client for interacting with Bundesbank exchange rate APIs.
 */
@FeignClient(
    name = "bundesbankClient",
    url = "${feign.clients.bundesbank.url}",
    configuration = BundesbankFeignConfig.class
)
public interface BundesbankFeignClient {

    /**
     * Fetches exchange rates within a given date range.
     */
    @GetMapping(produces = "application/vnd.sdmx.data+json")
    ExchangeRateApiResponse getExchangeRatesByDate(
        @RequestParam("startPeriod") String startPeriod,
        @RequestParam("endPeriod") String endPeriod,
        @RequestParam(value = "detail", defaultValue = "dataonly") String detail
    );

    /**
     * Fetches all available exchange rates.
     */
    @GetMapping(produces = "application/vnd.sdmx.data+json")
    ExchangeRateApiResponse getAllExchangeRates(
        @RequestParam(value = "detail", defaultValue = "dataonly") String detail
    );
}