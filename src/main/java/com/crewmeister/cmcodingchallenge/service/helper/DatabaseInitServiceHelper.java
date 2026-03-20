package com.crewmeister.cmcodingchallenge.service.helper;

import com.crewmeister.cmcodingchallenge.exception.CurrencyNotFoundException;
import com.crewmeister.cmcodingchallenge.external.feign.response.DimensionValue;
import com.crewmeister.cmcodingchallenge.external.feign.response.ExchangeRateApiResponse;
import com.crewmeister.cmcodingchallenge.model.Currency;
import com.crewmeister.cmcodingchallenge.model.ExchangeRate;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;
import org.springframework.stereotype.Service;

@Service
public class DatabaseInitServiceHelper {


    public List<ExchangeRate> mapExchangeRateToEntity(ExchangeRateApiResponse response, Map<String, Currency> persistedCurrencies) {

        var data = response.data();
        List<DimensionValue> currencyDim = this.extractAllCurrencies(response);
        List<DimensionValue> dateDim = this.extractAllDates(response);

        return data.dataSets().parallelStream()
            .flatMap(ds -> ds.series().entrySet().stream())
            .flatMap(entry -> {

                String[] key = entry.getKey().split(Pattern.quote(":"));
                String currencyCode = currencyDim.get(Integer.parseInt(key[1])).id();
                Currency currency = Optional.of(persistedCurrencies.get(currencyCode)).orElseThrow(
                    () -> new CurrencyNotFoundException(String.format("Currency %s not found while initializing database", currencyCode)));
                return entry.getValue().observations().entrySet().stream()
                    .map(obs -> {

                        ExchangeRate e = new ExchangeRate();

                        // exchange rate
                        if (obs.getValue() != null && !obs.getValue().isEmpty() && obs.getValue().get(0) != null) {
                            e.setExchangeRate(Double.parseDouble((String) obs.getValue().getFirst()));
                        }
                        // date
                        e.setDate(LocalDate.parse(
                            dateDim.get(Integer.parseInt(obs.getKey())).id()
                        ));
                        // currency
                        e.setCurrency(currency);

                        return e;
                    });
            })
            .toList();
    }

    // Extract available dates from data
    public List<DimensionValue> extractAllDates(ExchangeRateApiResponse data) {
        return data.data().structure().dimensions().observation().stream().filter(x -> x.id().contentEquals("TIME_PERIOD")).findAny()
            .orElseThrow(() -> new CurrencyNotFoundException("TIME_PERIOD not found.")).values();
    }

    // Extract available currencies from data
    public List<DimensionValue> extractAllCurrencies(ExchangeRateApiResponse bundesbankResponse) {
        return bundesbankResponse.data().structure().dimensions().series().stream().filter(x -> x.id().contentEquals("BBK_STD_CURRENCY")).findAny()
            .orElseThrow(() -> new CurrencyNotFoundException("BBK_STD_CURRENCY not found."))
            .values();
    }
}
