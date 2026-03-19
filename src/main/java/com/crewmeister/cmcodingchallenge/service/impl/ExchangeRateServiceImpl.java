package com.crewmeister.cmcodingchallenge.service.impl;

import com.crewmeister.cmcodingchallenge.dto.ExchangeRateDto;
import com.crewmeister.cmcodingchallenge.dto.ExchangeRateGroupedResponse;
import com.crewmeister.cmcodingchallenge.dto.ExchangeRatePayload;
import com.crewmeister.cmcodingchallenge.dto.RateDto;
import com.crewmeister.cmcodingchallenge.exception.ExchangeRateNotFoundException;
import com.crewmeister.cmcodingchallenge.model.ExchangeRate;
import com.crewmeister.cmcodingchallenge.repository.ExchangeRateRepository;
import com.crewmeister.cmcodingchallenge.service.IExchangeRateService;
import com.crewmeister.cmcodingchallenge.service.helper.ExchangeRateHelperService;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExchangeRateServiceImpl implements IExchangeRateService {

    private final ExchangeRateRepository exchangeRateRepository;
    private final ExchangeRateHelperService exchangeRateHelperService;

    @Cacheable("get-all-exchange-rates")
    @Override
    public PageImpl<ExchangeRateGroupedResponse> getAllExchangeRates(int page, int size, boolean sortByDateAsc, boolean ignoreNullRates) {

        List<ExchangeRateDto> persistedExchangeRates = exchangeRateRepository.findAllRates();
        List<ExchangeRateGroupedResponse> formattedResult =
            exchangeRateHelperService.formatResult(persistedExchangeRates, sortByDateAsc, ignoreNullRates);

        // Apply pagination manually
        int start = Math.min(page * size, formattedResult.size());
        int end = Math.min(start + size, formattedResult.size());
        List<ExchangeRateGroupedResponse> pageContent = formattedResult.subList(start, end);

        return new PageImpl<>(pageContent, PageRequest.of(page, size), formattedResult.size());
    }

    @Cacheable("get-exchange-rates-by-date")
    @Override
    public ExchangeRateGroupedResponse getExchangeRatesByDate(LocalDate date, boolean ignoreNullRates) {
        List<ExchangeRateDto> persistedExchangeRates = exchangeRateRepository.findByDate(date);
        return exchangeRateHelperService.formatResult(persistedExchangeRates, true, ignoreNullRates).stream().findFirst()
            .orElseThrow(() -> new ExchangeRateNotFoundException(
                "Exchange rate not found for date=" + date
            ));
    }

    @Cacheable("get-exchange-rates-by-date-and-currency")
    @Override
    public RateDto getExchangeRateByCurrencyAndDate(String currencyCode, LocalDate date) {

        ExchangeRate rate = exchangeRateRepository.findByCurrencyCurrencyCodeAndDate(currencyCode, date)
            .orElseThrow(() -> new ExchangeRateNotFoundException(
                "Exchange rate not found for currency=" + currencyCode + " and date=" + date
            ));
        return new RateDto(rate.getExchangeRate());
    }

    @Cacheable("convert-to-euro")
    @Override
    public RateDto convertToEuro(ExchangeRatePayload payload) {
        ExchangeRate persistedExchangeRate = exchangeRateRepository.findByCurrencyCurrencyCodeAndDate(payload.currencyCode(),
            payload.date()).orElseThrow(() -> new ExchangeRateNotFoundException(
            "Exchange rate not found for currency=" + payload.currencyCode() + " and date=" + payload.date()
        ));
        return new RateDto(exchangeRateHelperService.convertAmountToEuro(persistedExchangeRate.getExchangeRate(), payload.exchangeAmount()));
    }
}