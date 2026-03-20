package com.crewmeister.cmcodingchallenge.repository;

import com.crewmeister.cmcodingchallenge.model.Currency;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CurrencyRepositoryTest {
    @Autowired
    private CurrencyRepository currencyRepository;

    @Test
    void findAllCurrencyCodes_returnsPage() {
        Currency currency = new Currency();
        currency.setCurrencyCode("EUR");
        currencyRepository.save(currency);
        Page<String> page = currencyRepository.findAllCurrencyCodes(PageRequest.of(0, 10));
        assertTrue(page.getContent().contains("EUR"));
    }
}

