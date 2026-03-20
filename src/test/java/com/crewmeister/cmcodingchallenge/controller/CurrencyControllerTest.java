package com.crewmeister.cmcodingchallenge.controller;

import com.crewmeister.cmcodingchallenge.service.ICurrencyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CurrencyControllerTest {

    @Mock
    private ICurrencyService currencyService;

    @InjectMocks
    private CurrencyController currencyController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getCurrencies_returnsCurrencies_whenFound() {
        Page<String> page = new PageImpl<>(List.of("EUR", "USD"));
        when(currencyService.getAllCurrencies(0, 10, true)).thenReturn(page);

        ResponseEntity<Page<String>> response = currencyController.getCurrencies(0, 10, true);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().getTotalElements());
    }

    @Test
    void getCurrencies_returnsNotFound_whenEmpty() {
        Page<String> emptyPage = Page.empty();
        when(currencyService.getAllCurrencies(0, 10, true)).thenReturn(emptyPage);

        ResponseEntity<Page<String>> response = currencyController.getCurrencies(0, 10, true);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
    }
}

