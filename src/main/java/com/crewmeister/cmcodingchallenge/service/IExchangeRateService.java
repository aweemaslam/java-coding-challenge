package com.crewmeister.cmcodingchallenge.service;

import com.crewmeister.cmcodingchallenge.dto.ExchangeRateGroupedResponse;
import com.crewmeister.cmcodingchallenge.dto.ExchangeRatePayload;
import com.crewmeister.cmcodingchallenge.dto.RateDto;
import jakarta.validation.Valid;
import java.time.LocalDate;
import org.springframework.data.domain.PageImpl;

public interface IExchangeRateService {

    PageImpl<ExchangeRateGroupedResponse> getAllExchangeRates(int page, int size, boolean sortByDateAsc, boolean ignoreNullRates);

    ExchangeRateGroupedResponse getExchangeRatesByDate(LocalDate date, boolean ignoreNullRates);

    RateDto getExchangeRateByCurrencyAndDate(String currencyCode, LocalDate date);

    RateDto convertToEuro(@Valid ExchangeRatePayload payload);
}
