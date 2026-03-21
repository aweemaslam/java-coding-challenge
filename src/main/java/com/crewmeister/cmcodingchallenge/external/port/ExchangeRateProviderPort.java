package com.crewmeister.cmcodingchallenge.external.port;

import com.crewmeister.cmcodingchallenge.external.feign.response.ExchangeRateApiResponse;
import java.time.LocalDate;

/**
 * Port interface for retrieving exchange rates from external providers.
 */
public interface ExchangeRateProviderPort {

    /**
     * Gets exchange rates for the specified date, or all rates if date is null.
     */
    ExchangeRateApiResponse getRates(LocalDate date);

}