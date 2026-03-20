package com.crewmeister.cmcodingchallenge.service.scheduler;

import com.crewmeister.cmcodingchallenge.external.feign.response.DimensionValue;
import com.crewmeister.cmcodingchallenge.external.feign.response.ExchangeRateApiResponse;
import com.crewmeister.cmcodingchallenge.external.port.ExchangeRateProviderPort;
import com.crewmeister.cmcodingchallenge.model.Currency;
import com.crewmeister.cmcodingchallenge.model.ExchangeRate;
import com.crewmeister.cmcodingchallenge.repository.CurrencyRepository;
import com.crewmeister.cmcodingchallenge.repository.ExchangeRateRepository;
import com.crewmeister.cmcodingchallenge.service.helper.DatabaseInitServiceHelper;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DatabaseInitService {

    private final DatabaseInitServiceHelper exchangeRateUpdateServiceHelper;
    private final ExchangeRateRepository exchangeRateRepository;
    private final CurrencyRepository currencyRepository;
    private final ExchangeRateProviderPort exchangeRateProviderPort;
    private final CacheManager cacheManager;

    @Value("${app.scheduler.zone:Europe/Berlin}")
    private String schedulerZone;

    // Schedule cron job on 4PM CEST every day to update DB with the latest data from API
    @Scheduled(
        cron = "${app.scheduler.cron:0 0 16 * * ?}",
        zone = "${app.scheduler.zone:Europe/Berlin}"
    )
    @SchedulerLock(name = "updateDB", lockAtLeastFor = "PT5M", lockAtMostFor = "PT10M")
    public void updateDB() {
        ExchangeRateApiResponse allRates = exchangeRateProviderPort.getRates(LocalDate.now(ZoneId.of(schedulerZone)));
        updateDB(allRates);
    }

    // for setup db on server start
    //@EventListener(ApplicationReadyEvent.class)
    @PostConstruct
    public void initDatabase() {
        ExchangeRateApiResponse allRates = exchangeRateProviderPort.getRates(null);
        updateDB(allRates);
    }

    private void updateDB(ExchangeRateApiResponse allRates) {
        if (allRates == null) {
            return;
        }
        this.initializeCurrencyTable(allRates);
        this.initializeExchangeRateTable(allRates);
        rebuildCaches();
    }

    //Initialize currencies table in DB
    @Transactional
    public void initializeCurrencyTable(ExchangeRateApiResponse data) {
        List<String> persistedCurrencies = currencyRepository.findAll().stream().map(Currency::getCurrencyCode).toList();
        List<DimensionValue> currencies = exchangeRateUpdateServiceHelper.extractAllCurrencies(data);
        currencyRepository.saveAll(
            currencies.stream().map(x -> new Currency(x.id())).filter(curr -> !persistedCurrencies.contains(curr.getCurrencyCode())).toList());
    }

    //Fill Exchange Rate Table in DB
    @Transactional
    public void initializeExchangeRateTable(ExchangeRateApiResponse response) {
        Map<String, Currency> persistedCurrencies = currencyRepository.findAll().stream()
            .collect(Collectors.toMap(Currency::getCurrencyCode, c -> c));
        List<ExchangeRate> exchangeRates = exchangeRateUpdateServiceHelper.mapExchangeRateToEntity(response, persistedCurrencies);
        //TODO: not a good approach to avoid duplicate enteries, need proper fix
        Set<String> existingKeys = exchangeRateRepository.findAll().parallelStream()
            .map(er -> er.getCurrency().getCurrencyCode() + "|" + er.getDate())
            .collect(Collectors.toSet());
        exchangeRates = exchangeRates.parallelStream()
            .filter(er -> !existingKeys.contains(er.getCurrency().getCurrencyCode() + "|" + er.getDate()))
            .toList();
        exchangeRateRepository.saveAll(exchangeRates);
    }

    // rebuild cache every time cron runs
    public void rebuildCaches() {
        cacheManager.getCacheNames().forEach(cache -> Objects.requireNonNull(cacheManager.getCache(cache)).clear());
    }
}
