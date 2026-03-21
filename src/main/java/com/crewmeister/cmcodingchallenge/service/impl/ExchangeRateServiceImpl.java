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

/**
 * Implementation of IExchangeRateService for managing exchange rate operations.
 */
@Service
@RequiredArgsConstructor
public class ExchangeRateServiceImpl implements IExchangeRateService {

    private final ExchangeRateRepository exchangeRateRepository;
    private final ExchangeRateHelperService exchangeRateHelperService;

    /**
     * Retrieves all exchange rates, grouped by date, paginated, and optionally sorted.
     */
    @Cacheable("get-all-exchange-rates")
    @Override
    public PageImpl<ExchangeRateGroupedResponse> getAllExchangeRates(int page, int size, boolean sortByDateAsc, boolean ignoreNullRates) {

        List<ExchangeRateDto> persistedExchangeRates = exchangeRateRepository.findAllRates();
        List<ExchangeRateGroupedResponse> formattedResult =
            exchangeRateHelperService.formatResult(persistedExchangeRates, sortByDateAsc, ignoreNullRates);

        // Apply pagination manually on date attribute
        int start = Math.min(page * size, formattedResult.size());
        int end = Math.min(start + size, formattedResult.size());
        List<ExchangeRateGroupedResponse> pageContent = formattedResult.subList(start, end);

        return new PageImpl<>(pageContent, PageRequest.of(page, size), formattedResult.size());
    }

    /**
     * Retrieves exchange rates for a specific date.
     *
     * @param date           Date to fetch exchange rates for
     * @param ignoreNullRates True to include null rates
     * @return Grouped exchange rate response
     */
    @Cacheable("get-exchange-rates-by-date")
    @Override
    public ExchangeRateGroupedResponse getExchangeRatesByDate(LocalDate date, boolean ignoreNullRates) {
        List<ExchangeRateDto> persistedExchangeRates = exchangeRateRepository.findByDate(date);
        return exchangeRateHelperService.formatResult(persistedExchangeRates, true, ignoreNullRates).stream().findFirst()
            .orElseThrow(() -> new ExchangeRateNotFoundException(
                "Exchange rate not found for date=" + date
            ));
    }

    /**
     * Retrieves exchange rate for a specific currency and date.
     */
    @Cacheable("get-exchange-rates-by-date-and-currency")
    @Override
    public RateDto getExchangeRateByCurrencyAndDate(String currencyCode, LocalDate date) {

        ExchangeRate rate = exchangeRateRepository.findByCurrencyCurrencyCodeAndDate(currencyCode, date)
            .orElseThrow(() -> new ExchangeRateNotFoundException(
                "Exchange rate not found for currency=" + currencyCode + " and date=" + date
            ));
        return new RateDto(rate.getExchangeRate());
    }

    /**
     * Converts a given amount to Euro using the exchange rate for the currency and date.
     */
    @Cacheable("convert-to-euro")
    @Override
    public RateDto convertToEuro(ExchangeRatePayload payload) {
        ExchangeRate persistedExchangeRate = exchangeRateRepository.findByCurrencyCurrencyCodeAndDate(
            payload.currencyCode(), payload.date()
        ).orElseThrow(() -> new ExchangeRateNotFoundException(
            "Exchange rate not found for currency=" + payload.currencyCode() + " and date=" + payload.date()
        ));
        return new RateDto(
            exchangeRateHelperService.convertAmountToEuro(
                persistedExchangeRate.getExchangeRate(),
                payload.exchangeAmount()
            )
        );
    }
}