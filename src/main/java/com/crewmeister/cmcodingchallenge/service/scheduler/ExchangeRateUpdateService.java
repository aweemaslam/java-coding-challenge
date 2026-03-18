package com.crewmeister.cmcodingchallenge.service.scheduler;

import com.crewmeister.cmcodingchallenge.exception.CurrencyNotFoundException;
import com.crewmeister.cmcodingchallenge.external.feign.response.DimensionValue;
import com.crewmeister.cmcodingchallenge.external.feign.response.ExchangeRateApiResponse;
import com.crewmeister.cmcodingchallenge.external.port.ExchangeRateProviderPort;
import com.crewmeister.cmcodingchallenge.model.Currency;
import com.crewmeister.cmcodingchallenge.model.ExchangeRate;
import com.crewmeister.cmcodingchallenge.repository.CurrencyRepository;
import com.crewmeister.cmcodingchallenge.repository.ExchangeRateRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cache.CacheManager;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExchangeRateUpdateService {

    private final ExchangeRateRepository exchangeRateRepository;
    private final CurrencyRepository currencyRepository;
    private final ExchangeRateProviderPort exchangeRateProviderPort;
    private final CacheManager cacheManager;

    // Schedule cron job on 4PM CEST every day to update DB with the latest data from API
    @Scheduled(cron = "0 0 16 * * ?", zone = "Europe/Berlin")
    public void updateDB() {
        ExchangeRateApiResponse allRates = exchangeRateProviderPort.getRates(LocalDate.now(ZoneOffset.of("Europe/Berlin")));
        updateDB(allRates);
    }

    // for setup db on server start
    @EventListener(ApplicationReadyEvent.class)
    public void initDB() {
        ExchangeRateApiResponse allRates = exchangeRateProviderPort.getRates(null);
        updateDB(allRates);
    }

    private void updateDB(ExchangeRateApiResponse allRates) {
        this.initializeCurrencyTable(allRates);
        this.initializeExchangeRateTable(allRates);
        rebuildCaches();
    }

    //Initialize currencies table in DB
    @Transactional
    public void initializeCurrencyTable(ExchangeRateApiResponse data) {
        List<DimensionValue> currencies = this.extractAllCurrencies(data);
        currencyRepository.saveAll(currencies.stream().map(x -> new Currency(x.id())).toList());
    }

    //Fill Exchange Rate Table in DB
    @Transactional
    public void initializeExchangeRateTable(ExchangeRateApiResponse response) {
        List<ExchangeRate> exchangeRates = mapExchangeRateToEntity(response);
        exchangeRateRepository.saveAll(exchangeRates);
    }

    // Extract available currencies from data
    public List<DimensionValue> extractAllCurrencies(ExchangeRateApiResponse bundesbankResponse) {
        return bundesbankResponse.data().structure().dimensions().series().stream().filter(x -> x.id().contentEquals("BBK_STD_CURRENCY")).findAny()
            .orElseThrow(() -> new CurrencyNotFoundException("BBK_STD_CURRENCY not found."))
            .values();
    }

    // Extract available dates from data
    public List<DimensionValue> extractAllDates(ExchangeRateApiResponse data) {
        return data.data().structure().dimensions().observation().stream().filter(x -> x.id().contentEquals("TIME_PERIOD")).findAny()
            .orElseThrow(() -> new CurrencyNotFoundException("TIME_PERIOD not found.")).values();
    }

    public List<ExchangeRate> mapExchangeRateToEntity(ExchangeRateApiResponse response) {
        List<Currency> persistedCurrencies = currencyRepository.findAll();
        var data = response.data();
        List<DimensionValue> currencyDim = this.extractAllCurrencies(response);
        List<DimensionValue> dateDim = this.extractAllDates(response);

        return data.dataSets().parallelStream()
            .flatMap(ds -> ds.series().entrySet().stream())
            .flatMap(entry -> {

                String[] key = entry.getKey().split(Pattern.quote(":"));
                String currencyCode = currencyDim.get(Integer.parseInt(key[1])).id();

                Currency currency = persistedCurrencies.stream().filter(x -> x.getCurrencyCode().contentEquals(currencyCode)).findAny()
                    .orElseThrow(
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

    // rebuild cache every time cron runs
    public void rebuildCaches() {
        cacheManager.getCacheNames().forEach(cache -> Objects.requireNonNull(cacheManager.getCache(cache)).clear());
    }
}
