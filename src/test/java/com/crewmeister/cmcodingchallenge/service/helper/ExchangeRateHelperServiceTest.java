package com.crewmeister.cmcodingchallenge.service.helper;

import com.crewmeister.cmcodingchallenge.dto.CurrencyAndRateKeyValue;
import com.crewmeister.cmcodingchallenge.dto.ExchangeRateDto;
import com.crewmeister.cmcodingchallenge.dto.ExchangeRateGroupedResponse;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ExchangeRateHelperServiceTest {
    private final ExchangeRateHelperService helperService = new ExchangeRateHelperService();

    @Test
    void formatResult_groupsAndSortsCorrectly() {
        ExchangeRateDto dto1 = new ExchangeRateDto("USD",  LocalDate.of(2024,1,1), 1.1);
        ExchangeRateDto dto2 = new ExchangeRateDto("EUR", LocalDate.of(2024,1,1), 1.0);
        ExchangeRateDto dto3 = new ExchangeRateDto("GBP", LocalDate.of(2024,1,2), 0.9);
        List<ExchangeRateDto> dtos = List.of(dto1, dto2, dto3);
        List<ExchangeRateGroupedResponse> result = helperService.formatResult(dtos, true, false);
        assertEquals(2, result.size());
        assertEquals("2024-01-01", result.get(0).date());
        assertEquals(2, result.get(0).rates().size());
    }

    @Test
    void convertAmountToEuro_dividesCorrectly() {
        double euro = helperService.convertAmountToEuro(2.0, 10.0);
        assertEquals(5.0, euro);
    }
}

