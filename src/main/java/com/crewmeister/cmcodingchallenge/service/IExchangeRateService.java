package com.crewmeister.cmcodingchallenge.service;

import com.crewmeister.cmcodingchallenge.dto.ExchangeRateGroupedResponse;
import com.crewmeister.cmcodingchallenge.dto.ExchangeRatePayload;
import com.crewmeister.cmcodingchallenge.dto.RateDto;
import jakarta.validation.Valid;
import java.time.LocalDate;
import org.springframework.data.domain.PageImpl;

/**
 * Service interface for managing exchange rate operations.
 */
public interface IExchangeRateService {

    /**
     * Retrieves all exchange rates, grouped by date, paginated, and optionally sorted.
     *
     * @param page           Page number (0-based)
     * @param size           Number of records per page
     * @param sortByDateAsc  True to sort dates ascending, false for descending
     * @param ignoreNullRates True to include records with null rates
     * @return Paginated list of grouped exchange rate responses
     */
    PageImpl<ExchangeRateGroupedResponse> getAllExchangeRates(int page, int size, boolean sortByDateAsc, boolean ignoreNullRates);

    /**
     * Retrieves exchange rates for a specific date.
     *
     * @param date            Date to fetch exchange rates for
     * @param ignoreNullRates True to include null rates
     * @return Grouped exchange rate response
     */
    ExchangeRateGroupedResponse getExchangeRatesByDate(LocalDate date, boolean ignoreNullRates);

    /**
     * Retrieves exchange rate for a specific currency and date.
     *
     * @param currencyCode Currency code
     * @param date         Date to fetch exchange rate for
     * @return RateDto containing the exchange rate
     */
    RateDto getExchangeRateByCurrencyAndDate(String currencyCode, LocalDate date);

    /**
     * Converts an amount to Euro using the exchange rate for the given currency and date.
     *
     * @param payload Payload containing currency code, amount, and date
     * @return RateDto with converted Euro amount
     */
    RateDto convertToEuro(@Valid ExchangeRatePayload payload);
}