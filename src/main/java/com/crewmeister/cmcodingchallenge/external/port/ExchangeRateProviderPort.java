package com.crewmeister.cmcodingchallenge.external.port;

import com.crewmeister.cmcodingchallenge.external.feign.response.ExchangeRateApiResponse;
import java.time.LocalDate;

public interface ExchangeRateProviderPort {

    ExchangeRateApiResponse getRates(LocalDate date);

}