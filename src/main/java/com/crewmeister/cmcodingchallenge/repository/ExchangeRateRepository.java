package com.crewmeister.cmcodingchallenge.repository;

import com.crewmeister.cmcodingchallenge.model.Currency;
import com.crewmeister.cmcodingchallenge.model.ExchangeRate;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Long> {
    Optional<ExchangeRate> findByCurrencyAndDate(Currency currency, LocalDate date);

    @Query("SELECT er FROM ExchangeRate er WHERE er.currency.currencyCode IN :codes AND er.date = :date")
    List<ExchangeRate> findByCurrencyCurrencyCodeAndDate(Set<String> codes, LocalDate date);
}