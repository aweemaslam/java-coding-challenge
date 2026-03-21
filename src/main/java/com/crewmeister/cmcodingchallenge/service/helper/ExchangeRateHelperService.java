package com.crewmeister.cmcodingchallenge.service.helper;

import com.crewmeister.cmcodingchallenge.dto.CurrencyAndRateKeyValue;
import com.crewmeister.cmcodingchallenge.dto.ExchangeRateDto;
import com.crewmeister.cmcodingchallenge.dto.ExchangeRateGroupedResponse;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Helper service for formatting and processing exchange rate data.
 */
@Service
@RequiredArgsConstructor
public class ExchangeRateHelperService {

    /**
     * Groups exchange rate records by date and formats them into grouped responses.
     *
     * @param persistedRecords List of exchange rate DTOs
     * @param sortByDateAsc    True to sort results by ascending date
     * @param ignoreNullRates  True to include records with null rates
     * @return List of grouped exchange rate responses
     */
    public List<ExchangeRateGroupedResponse> formatResult(
        List<ExchangeRateDto> persistedRecords,
        boolean sortByDateAsc,
        boolean ignoreNullRates
    ) {

        // Group records by date first
        Map<String, List<ExchangeRateDto>> groupedByDate = persistedRecords.parallelStream()
            .filter(dto -> (dto.rate() != null) || ignoreNullRates) // ignore null rates if specified
            .collect(Collectors.groupingBy(dto -> dto.date().toString()));

        // Transform into ExchangeRateGroupedResponse
        return groupedByDate.entrySet().parallelStream()
            .map(entry -> new ExchangeRateGroupedResponse(
                entry.getKey(), // date
                entry.getValue().stream()
                    .map(dto -> new CurrencyAndRateKeyValue(dto.currency(), dto.rate()))
                    .toList()
            ))
            .sorted(sortByDateAsc
                ? Comparator.comparing(ExchangeRateGroupedResponse::date)
                : Comparator.comparing(ExchangeRateGroupedResponse::date).reversed())
            .toList();
    }

    /**
     * Converts an amount in a foreign currency to Euros using the exchange rate.
     *
     * @param exchangeRate   Exchange rate for the currency
     * @param exchangeAmount Amount to convert
     * @return Equivalent amount in Euros
     */
    public Double convertAmountToEuro(Double exchangeRate, Double exchangeAmount) {
        return exchangeAmount / exchangeRate;
    }
}