package com.crewmeister.cmcodingchallenge.repository;

import com.crewmeister.cmcodingchallenge.model.Currency;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Repository for accessing Currency entities and their codes.
 */
@Repository("currencyRepository")
public interface CurrencyRepository extends JpaRepository<Currency, Long> {

    /**
     * Retrieves all currency codes with pagination and optional sorting.
     */
    @Query("SELECT c.currencyCode FROM Currency c")
    Page<String> findAllCurrencyCodes(Pageable sortedByName);
}