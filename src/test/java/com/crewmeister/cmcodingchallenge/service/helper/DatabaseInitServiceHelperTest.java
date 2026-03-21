package com.crewmeister.cmcodingchallenge.service.helper;

import com.crewmeister.cmcodingchallenge.exception.CurrencyNotFoundException;
import com.crewmeister.cmcodingchallenge.external.feign.response.DimensionValue;
import com.crewmeister.cmcodingchallenge.external.feign.response.ExchangeRateApiResponse;
import com.crewmeister.cmcodingchallenge.external.feign.response.Data;
import com.crewmeister.cmcodingchallenge.external.feign.response.DataSet;
import com.crewmeister.cmcodingchallenge.external.feign.response.Dimension;
import com.crewmeister.cmcodingchallenge.external.feign.response.Dimensions;
import com.crewmeister.cmcodingchallenge.external.feign.response.Series;
import com.crewmeister.cmcodingchallenge.external.feign.response.Structure;
import com.crewmeister.cmcodingchallenge.model.Currency;
import com.crewmeister.cmcodingchallenge.model.ExchangeRate;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseInitServiceHelperTest {
    private final DatabaseInitServiceHelper helper = new DatabaseInitServiceHelper();

    @Test
    void extractAllDates_returnsDates() {
        DimensionValue date1 = new DimensionValue("2024-01-01", "2024-01-01");
        Dimension dateDim = new Dimension("TIME_PERIOD", List.of(date1));
        Dimensions dims = new Dimensions(List.of(), List.of(dateDim));
        Structure structure = new Structure(dims);
        Data data = new Data(structure, List.of());
        ExchangeRateApiResponse response = new ExchangeRateApiResponse(data);
        List<DimensionValue> result = helper.extractAllDates(response);
        assertEquals(1, result.size());
        assertEquals("2024-01-01", result.get(0).id());
    }

    @Test
    void extractAllCurrencies_returnsCurrencies() {
        DimensionValue cur1 = new DimensionValue("USD", "USD");
        Dimension curDim = new Dimension("BBK_STD_CURRENCY", List.of(cur1));
        Dimensions dims = new Dimensions(List.of(curDim), List.of());
        Structure structure = new Structure(dims);
        Data data = new Data(structure, List.of());
        ExchangeRateApiResponse response = new ExchangeRateApiResponse(data);
        List<DimensionValue> result = helper.extractAllCurrencies(response);
        assertEquals(1, result.size());
        assertEquals("USD", result.get(0).id());
    }

    @Test
    void extractAllDates_throwsIfNotFound() {
        Dimensions dims = new Dimensions(List.of(), List.of());
        Structure structure = new Structure(dims);
        Data data = new Data(structure, List.of());
        ExchangeRateApiResponse response = new ExchangeRateApiResponse(data);
        assertThrows(CurrencyNotFoundException.class, () -> helper.extractAllDates(response));
    }

    @Test
    void extractAllCurrencies_throwsIfNotFound() {
        Dimensions dims = new Dimensions(List.of(), List.of());
        Structure structure = new Structure(dims);
        Data data = new Data(structure, List.of());
        ExchangeRateApiResponse response = new ExchangeRateApiResponse(data);
        assertThrows(CurrencyNotFoundException.class, () -> helper.extractAllCurrencies(response));
    }

    @Test
    void mapExchangeRateToEntity_mapsCorrectly() {
        // Setup dimensions
        DimensionValue cur1 = new DimensionValue("USD", "USD");
        DimensionValue date1 = new DimensionValue("2024-01-01", "2024-01-01");
        Dimension curDim = new Dimension("BBK_STD_CURRENCY", List.of(cur1));
        Dimension dateDim = new Dimension("TIME_PERIOD", List.of(date1));
        Dimensions dims = new Dimensions(List.of(curDim), List.of(dateDim));
        Structure structure = new Structure(dims);
        // Setup series/observations
        Series series = new Series(Map.of("0", List.of("1.23")));
        Map<String, Series> seriesMap = Map.of("0:0", series);
        DataSet dataSet = new DataSet(null, null, seriesMap);
        Data data = new Data(structure, List.of(dataSet));
        ExchangeRateApiResponse response = new ExchangeRateApiResponse(data);
        Currency currency = new Currency("USD");
        Map<String, Currency> persisted = Map.of("USD", currency);
        List<ExchangeRate> result = helper.mapExchangeRateToEntity(response, persisted);
        assertEquals(1, result.size());
        assertEquals(currency, result.get(0).getCurrency());
        assertEquals(LocalDate.parse("2024-01-01"), result.get(0).getDate());
        assertEquals(1.23, result.get(0).getExchangeRate());
    }
}
