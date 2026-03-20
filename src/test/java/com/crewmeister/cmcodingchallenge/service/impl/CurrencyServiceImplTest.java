package com.crewmeister.cmcodingchallenge.service.impl;

import com.crewmeister.cmcodingchallenge.repository.CurrencyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CurrencyServiceImplTest {
    @Mock
    private CurrencyRepository currencyRepository;
    @InjectMocks
    private CurrencyServiceImpl currencyService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllCurrencies_returnsPage() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<String> page = new PageImpl<>(List.of("EUR", "USD"));
        when(currencyRepository.findAllCurrencyCodes(any(Pageable.class))).thenReturn(page);
        Page<String> result = currencyService.getAllCurrencies(0, 10, true);
        assertEquals(2, result.getTotalElements());
        verify(currencyRepository, times(1)).findAllCurrencyCodes(any(Pageable.class));
    }
}

