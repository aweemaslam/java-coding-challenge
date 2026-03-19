package com.crewmeister.cmcodingchallenge.repository;

import com.crewmeister.cmcodingchallenge.dto.ExchangeRateDto;
import com.crewmeister.cmcodingchallenge.model.ExchangeRate;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Long> {

    @Query("SELECT er FROM ExchangeRate er WHERE er.currency.currencyCode = :code AND er.date = :date")
    Optional<ExchangeRate> findByCurrencyCurrencyCodeAndDate(String code, LocalDate date);

    @Query("SELECT new com.crewmeister.cmcodingchallenge.dto.ExchangeRateDto(c.currencyCode, e.date, e.exchangeRate) FROM ExchangeRate e "
        + "JOIN e.currency c")
    List<ExchangeRateDto> findAllRates();

    @Query("SELECT new com.crewmeister.cmcodingchallenge.dto.ExchangeRateDto(er.currency.currencyCode, er.date, er.exchangeRate) FROM ExchangeRate er WHERE er.date = :date")
    List<ExchangeRateDto> findByDate(LocalDate date);
}