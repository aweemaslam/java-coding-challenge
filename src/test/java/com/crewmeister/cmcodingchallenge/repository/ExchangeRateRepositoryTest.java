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

    @Test
    void findAllRates_returnsAllExchangeRateDtos() {
        Currency usd = new Currency();
        usd.setCurrencyCode("USD");
        currencyRepository.save(usd);
        Currency eur = new Currency();
        eur.setCurrencyCode("EUR");
        currencyRepository.save(eur);
        ExchangeRate rate1 = new ExchangeRate();
        rate1.setCurrency(usd);
        rate1.setDate(LocalDate.of(2024, 1, 1));
        rate1.setExchangeRate(1.1);
        exchangeRateRepository.save(rate1);
        ExchangeRate rate2 = new ExchangeRate();
        rate2.setCurrency(eur);
        rate2.setDate(LocalDate.of(2024, 1, 2));
        rate2.setExchangeRate(0.9);
        exchangeRateRepository.save(rate2);
        var dtos = exchangeRateRepository.findAllRates();
        assertEquals(2, dtos.size());
        assertTrue(dtos.stream().anyMatch(dto -> dto.currency().equals("USD") && dto.rate().equals(1.1)));
        assertTrue(dtos.stream().anyMatch(dto -> dto.currency().equals("EUR") && dto.rate().equals(0.9)));
    }

    @Test
    void findByDate_returnsExchangeRateDtosForDate() {
        Currency usd = new Currency();
        usd.setCurrencyCode("USD");
        currencyRepository.save(usd);
        ExchangeRate rate = new ExchangeRate();
        rate.setCurrency(usd);
        rate.setDate(LocalDate.of(2024, 3, 21));
        rate.setExchangeRate(1.5);
        exchangeRateRepository.save(rate);
        var dtos = exchangeRateRepository.findByDate(LocalDate.of(2024, 3, 21));
        assertEquals(1, dtos.size());
        assertEquals("USD", dtos.get(0).currency());
        assertEquals(1.5, dtos.get(0).rate());
    }
}
