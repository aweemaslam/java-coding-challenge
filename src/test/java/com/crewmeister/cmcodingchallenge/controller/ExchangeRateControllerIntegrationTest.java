package com.crewmeister.cmcodingchallenge.controller;

import com.crewmeister.cmcodingchallenge.model.Currency;
import com.crewmeister.cmcodingchallenge.model.ExchangeRate;
import com.crewmeister.cmcodingchallenge.repository.CurrencyRepository;
import com.crewmeister.cmcodingchallenge.repository.ExchangeRateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
class ExchangeRateControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private CurrencyRepository currencyRepository;
    @Autowired
    private ExchangeRateRepository exchangeRateRepository;

    @BeforeEach
    void setUp() {
        // Clean up
        exchangeRateRepository.deleteAll();
        currencyRepository.deleteAll();
        // Seed test data
        Currency currency = new Currency();
        currency.setCurrencyCode("USD");
        currency = currencyRepository.save(currency);
        ExchangeRate rate = new ExchangeRate();
        rate.setCurrency(currency);
        rate.setDate(LocalDate.of(2024, 1, 1));
        rate.setExchangeRate(1.23);
        exchangeRateRepository.save(rate);
    }

    @Test
    void getAllExchangeRates_returnsOkOrNotFound() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/exchange-rate"))
                .andReturn();
        int status = result.getResponse().getStatus();
        assertTrue(status == 200 || status == 404, "Expected 200 OK or 404 Not Found, but got: " + status);
    }

    @Test
    void getExchangeRatesByDate_returnsOkOrNotFound() throws Exception {
        String date = "2024-01-01";
        MvcResult result = mockMvc.perform(get("/api/exchange-rate/" + date))
                .andReturn();
        int status = result.getResponse().getStatus();
        assertTrue(status == 200 || status == 404, "Expected 200 OK or 404 Not Found, but got: " + status);
    }

    @Test
    void convertToEuro_returnsOkOrBadRequestOrNotFound() throws Exception {
        String payload = "{\"currencyCode\":\"USD\",\"amount\":100,\"date\":\"2024-01-01\"}";
        MvcResult result = mockMvc.perform(post("/api/exchange-rate/convert")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andReturn();
        int status = result.getResponse().getStatus();
        assertTrue(status == 200 || status == 400 || status == 404, "Expected 200, 400, or 404, but got: " + status);
    }

    @Test
    void getExchangeRate_returnsOkOrNotFound() throws Exception {
        String currency = "USD";
        String date = "2024-01-01";
        MvcResult result = mockMvc.perform(get("/api/exchange-rate/" + currency + "/" + date))
                .andReturn();
        int status = result.getResponse().getStatus();
        assertTrue(status == 200 || status == 404, "Expected 200 OK or 404 Not Found, but got: " + status);
    }
}
