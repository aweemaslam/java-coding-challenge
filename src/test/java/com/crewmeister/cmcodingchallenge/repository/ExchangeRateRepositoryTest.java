package com.crewmeister.cmcodingchallenge.repository;

import com.crewmeister.cmcodingchallenge.model.Currency;
import com.crewmeister.cmcodingchallenge.model.ExchangeRate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ExchangeRateRepositoryTest {
    @Autowired
    private ExchangeRateRepository exchangeRateRepository;
    @Autowired
    private CurrencyRepository currencyRepository;

    @Test
    void saveAndFindByCurrencyCurrencyCodeAndDate() {
        Currency currency = new Currency();
        currency.setCurrencyCode("USD");
        currencyRepository.save(currency);
        ExchangeRate rate = new ExchangeRate();
        rate.setCurrency(currency);
        rate.setDate(LocalDate.now());
        rate.setExchangeRate(1.23);
        exchangeRateRepository.save(rate);
        assertTrue(exchangeRateRepository.findByCurrencyCurrencyCodeAndDate("USD", rate.getDate()).isPresent());
    }
}

