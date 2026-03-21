package com.crewmeister.cmcodingchallenge.service.scheduler;

import com.crewmeister.cmcodingchallenge.external.feign.response.DimensionValue;
import com.crewmeister.cmcodingchallenge.external.feign.response.ExchangeRateApiResponse;
import com.crewmeister.cmcodingchallenge.model.Currency;
import com.crewmeister.cmcodingchallenge.model.ExchangeRate;
import com.crewmeister.cmcodingchallenge.repository.CurrencyRepository;
import com.crewmeister.cmcodingchallenge.repository.ExchangeRateRepository;
import com.crewmeister.cmcodingchallenge.service.helper.DatabaseInitServiceHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.cache.CacheManager;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DatabaseInitServiceTest {
    private DatabaseInitServiceHelper helper;
    private ExchangeRateRepository exchangeRateRepository;
    private CurrencyRepository currencyRepository;
    private com.crewmeister.cmcodingchallenge.external.port.ExchangeRateProviderPort exchangeRateProviderPort;
    private CacheManager cacheManager;
    private DatabaseInitService service;

    @BeforeEach
    void setUp() {
        helper = mock(DatabaseInitServiceHelper.class);
        exchangeRateRepository = mock(ExchangeRateRepository.class);
        currencyRepository = mock(CurrencyRepository.class);
        exchangeRateProviderPort = mock(com.crewmeister.cmcodingchallenge.external.port.ExchangeRateProviderPort.class);
        cacheManager = mock(CacheManager.class);
        service = new DatabaseInitService(helper, exchangeRateRepository, currencyRepository, exchangeRateProviderPort, cacheManager);
    }

    @Test
    void updateDB_nullRates_doesNothing() {
        service = spy(service);
        service.updateDB(null);
        verify(service, never()).initializeCurrencyTable(any());
        verify(service, never()).initializeExchangeRateTable(any());
        verify(service, never()).rebuildCaches();
    }

    @Test
    void initializeCurrencyTable_savesNewCurrencies() {
        when(currencyRepository.findAll()).thenReturn(List.of(new Currency("USD")));
        List<DimensionValue> dimVals = List.of(new DimensionValue("EUR", "Euro"), new DimensionValue("USD", "US Dollar"));
        when(helper.extractAllCurrencies(any())).thenReturn(dimVals);
        service.initializeCurrencyTable(mock(ExchangeRateApiResponse.class));
        ArgumentCaptor<List<Currency>> captor = ArgumentCaptor.forClass(List.class);
        verify(currencyRepository).saveAll(captor.capture());
        List<Currency> saved = captor.getValue();
        assertEquals(1, saved.size());
        assertEquals("EUR", saved.get(0).getCurrencyCode());
    }

    @Test
    void initializeExchangeRateTable_savesNonDuplicateRates() {
        Currency usd = new Currency("USD");
        usd.setId(1L);
        ExchangeRate existing = new ExchangeRate();
        existing.setCurrency(usd);
        existing.setDate(LocalDate.of(2024, 1, 1));
        when(currencyRepository.findAll()).thenReturn(List.of(usd));
        when(exchangeRateRepository.findAll()).thenReturn(List.of(existing));
        ExchangeRate newRate = new ExchangeRate();
        newRate.setCurrency(usd);
        newRate.setDate(LocalDate.of(2024, 1, 2));
        when(helper.mapExchangeRateToEntity(any(), any())).thenReturn(List.of(existing, newRate));
        service.initializeExchangeRateTable(mock(ExchangeRateApiResponse.class));
        ArgumentCaptor<List<ExchangeRate>> captor = ArgumentCaptor.forClass(List.class);
        verify(exchangeRateRepository).saveAll(captor.capture());
        List<ExchangeRate> saved = captor.getValue();
        assertEquals(1, saved.size());
        assertEquals(LocalDate.of(2024, 1, 2), saved.get(0).getDate());
    }

    @Test
    void rebuildCaches_clearsAllCaches() {
        var cache = mock(org.springframework.cache.Cache.class);
        when(cacheManager.getCacheNames()).thenReturn(Set.of("foo", "bar"));
        when(cacheManager.getCache(any())).thenReturn(cache);
        service.rebuildCaches();
        verify(cache, times(2)).clear();
    }
}

