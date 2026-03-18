package com.crewmeister.cmcodingchallenge.repository;

import com.crewmeister.cmcodingchallenge.model.Currency;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository("currencyRepository")
public interface CurrencyRepository extends JpaRepository<Currency, Long> {

    @Query("SELECT c.currencyCode FROM Currency c")
    Page<String> findAllCurrencyCodes(Pageable sortedByName);
    Optional<Currency> findByCurrencyCode(String currencyCode);
    List<Currency> findByCurrencyCodeIn(Set<String> currencyCodes);
}