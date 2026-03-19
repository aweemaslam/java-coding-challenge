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

@Service
@RequiredArgsConstructor
public class ExchangeRateHelperService {

    public List<ExchangeRateGroupedResponse> formatResult(List<ExchangeRateDto> persistedRecords, boolean sortByDateAsc, boolean ignoreNullRates) {

        // Group records by date first
        Map<String, List<ExchangeRateDto>> groupedByDate = persistedRecords.parallelStream()
            .filter(dto -> (dto.rate() != null) || ignoreNullRates) // ignore null rates
            .collect(Collectors.groupingBy(dto -> dto.date().toString()));

        // Transform into ExchangeRateGroupedResponse
        return groupedByDate.entrySet().parallelStream()
            .map(entry -> new ExchangeRateGroupedResponse(
                entry.getKey(), // date
                entry.getValue().stream()
                    .map(dto -> new CurrencyAndRateKeyValue(dto.currency(), dto.rate()))
                    .toList()
            ))
            .sorted(sortByDateAsc ? Comparator.comparing(ExchangeRateGroupedResponse::date)
                : Comparator.comparing(ExchangeRateGroupedResponse::date).reversed())
            .toList();
    }

    public Double convertAmountToEuro(Double exchangeRate, Double exchangeAmount) {
        return exchangeAmount / exchangeRate;
    }
}
