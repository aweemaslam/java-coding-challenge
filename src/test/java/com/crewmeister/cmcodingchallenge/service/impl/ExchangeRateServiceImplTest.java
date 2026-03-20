package com.crewmeister.cmcodingchallenge.service.impl;

import com.crewmeister.cmcodingchallenge.dto.ExchangeRateDto;
import com.crewmeister.cmcodingchallenge.dto.ExchangeRateGroupedResponse;
import com.crewmeister.cmcodingchallenge.dto.ExchangeRatePayload;
import com.crewmeister.cmcodingchallenge.dto.RateDto;
import com.crewmeister.cmcodingchallenge.exception.ExchangeRateNotFoundException;
import com.crewmeister.cmcodingchallenge.model.ExchangeRate;
import com.crewmeister.cmcodingchallenge.repository.ExchangeRateRepository;
import com.crewmeister.cmcodingchallenge.service.helper.ExchangeRateHelperService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ExchangeRateServiceImplTest {
    @Mock
    private ExchangeRateRepository exchangeRateRepository;
    @Mock
    private ExchangeRateHelperService exchangeRateHelperService;
    @InjectMocks
    private ExchangeRateServiceImpl exchangeRateService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllExchangeRates_returnsPage() {
        List<ExchangeRateDto> dtos = List.of();
        List<ExchangeRateGroupedResponse> grouped = List.of();
        when(exchangeRateRepository.findAllRates()).thenReturn(dtos);
        when(exchangeRateHelperService.formatResult(dtos, true, false)).thenReturn(grouped);
        PageImpl<ExchangeRateGroupedResponse> result = exchangeRateService.getAllExchangeRates(0, 10, true, false);
        assertNotNull(result);
    }

    @Test
    void getExchangeRatesByDate_throwsIfNotFound() {
        when(exchangeRateRepository.findByDate(any(LocalDate.class))).thenReturn(List.of());
        when(exchangeRateHelperService.formatResult(anyList(), anyBoolean(), anyBoolean())).thenReturn(List.of());
        assertThrows(Exception.class, () -> exchangeRateService.getExchangeRatesByDate(LocalDate.now(), false));
    }

    @Test
    void getExchangeRatesByDate_returnsGrouped() {
        List<ExchangeRateDto> dtos = List.of(mock(ExchangeRateDto.class));
        ExchangeRateGroupedResponse grouped = mock(ExchangeRateGroupedResponse.class);
        when(exchangeRateRepository.findByDate(any(LocalDate.class))).thenReturn(dtos);
        when(exchangeRateHelperService.formatResult(dtos, true, true)).thenReturn(List.of(grouped));
        ExchangeRateGroupedResponse result = exchangeRateService.getExchangeRatesByDate(LocalDate.now(), true);
        assertNotNull(result);
    }

    @Test
    void getExchangeRateByCurrencyAndDate_returnsRate() {
        ExchangeRate rate = new ExchangeRate();
        rate.setExchangeRate(1.23);
        when(exchangeRateRepository.findByCurrencyCurrencyCodeAndDate(eq("USD"), any(LocalDate.class))).thenReturn(Optional.of(rate));
        RateDto result = exchangeRateService.getExchangeRateByCurrencyAndDate("USD", LocalDate.now());
        assertNotNull(result);
        assertEquals(1.23, result.rate());
    }

    @Test
    void getExchangeRateByCurrencyAndDate_throwsIfNotFound() {
        when(exchangeRateRepository.findByCurrencyCurrencyCodeAndDate(eq("USD"), any(LocalDate.class))).thenReturn(Optional.empty());
        assertThrows(ExchangeRateNotFoundException.class, () -> exchangeRateService.getExchangeRateByCurrencyAndDate("USD", LocalDate.now()));
    }

    @Test
    void convertToEuro_returnsConverted() {
        ExchangeRate rate = new ExchangeRate();
        rate.setExchangeRate(2.0);
        ExchangeRatePayload payload = mock(ExchangeRatePayload.class);
        when(payload.currencyCode()).thenReturn("USD");
        when(payload.date()).thenReturn(LocalDate.now());
        when(payload.exchangeAmount()).thenReturn(10.0);
        when(exchangeRateRepository.findByCurrencyCurrencyCodeAndDate(eq("USD"), any(LocalDate.class))).thenReturn(Optional.of(rate));
        when(exchangeRateHelperService.convertAmountToEuro(2.0, 10.0)).thenReturn(5.0);
        RateDto result = exchangeRateService.convertToEuro(payload);
        assertNotNull(result);
        assertEquals(5.0, result.rate());
    }

    @Test
    void convertToEuro_throwsIfNotFound() {
        ExchangeRatePayload payload = mock(ExchangeRatePayload.class);
        when(payload.currencyCode()).thenReturn("USD");
        when(payload.date()).thenReturn(LocalDate.now());
        when(exchangeRateRepository.findByCurrencyCurrencyCodeAndDate(eq("USD"), any(LocalDate.class))).thenReturn(Optional.empty());
        assertThrows(ExchangeRateNotFoundException.class, () -> exchangeRateService.convertToEuro(payload));
    }
}
