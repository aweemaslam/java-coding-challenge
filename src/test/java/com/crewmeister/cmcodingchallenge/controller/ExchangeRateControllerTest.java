package com.crewmeister.cmcodingchallenge.controller;

import com.crewmeister.cmcodingchallenge.dto.ExchangeRateGroupedResponse;
import com.crewmeister.cmcodingchallenge.dto.ExchangeRatePayload;
import com.crewmeister.cmcodingchallenge.dto.RateDto;
import com.crewmeister.cmcodingchallenge.service.IExchangeRateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ExchangeRateControllerTest {
    @Mock
    private IExchangeRateService exchangeRateService;
    @InjectMocks
    private ExchangeRateController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllExchangeRates_returnsOk() {
        PageImpl<ExchangeRateGroupedResponse> page = new PageImpl<>(List.of(mock(ExchangeRateGroupedResponse.class)));
        when(exchangeRateService.getAllExchangeRates(0, 10, true, true)).thenReturn(page);
        ResponseEntity<PageImpl<ExchangeRateGroupedResponse>> response = controller.getAllExchangeRates(0, 10, true, true);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void getAllExchangeRates_returnsNotFound() {
        PageImpl<ExchangeRateGroupedResponse> emptyPage = new PageImpl<>(Collections.emptyList());
        when(exchangeRateService.getAllExchangeRates(0, 10, true, true)).thenReturn(emptyPage);
        ResponseEntity<PageImpl<ExchangeRateGroupedResponse>> response = controller.getAllExchangeRates(0, 10, true, true);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void getExchangeRatesByDate_returnsOk() {
        ExchangeRateGroupedResponse grouped = mock(ExchangeRateGroupedResponse.class);
        when(exchangeRateService.getExchangeRatesByDate(any(LocalDate.class), eq(true))).thenReturn(grouped);
        ResponseEntity<ExchangeRateGroupedResponse> response = controller.getExchangeRatesByDate(LocalDate.now(), true);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void getExchangeRatesByDate_returnsNotFound() {
        when(exchangeRateService.getExchangeRatesByDate(any(LocalDate.class), eq(true))).thenReturn(null);
        ResponseEntity<ExchangeRateGroupedResponse> response = controller.getExchangeRatesByDate(LocalDate.now(), true);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void convertToEuro_returnsOk() {
        RateDto dto = mock(RateDto.class);
        when(exchangeRateService.convertToEuro(any(ExchangeRatePayload.class))).thenReturn(dto);
        ResponseEntity<RateDto> response = controller.convertToEuro(mock(ExchangeRatePayload.class));
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void getExchangeRate_returnsOk() {
        RateDto dto = mock(RateDto.class);
        when(exchangeRateService.getExchangeRateByCurrencyAndDate(eq("USD"), any(LocalDate.class))).thenReturn(dto);
        ResponseEntity<RateDto> response = controller.getExchangeRate("USD", LocalDate.now());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }
}

