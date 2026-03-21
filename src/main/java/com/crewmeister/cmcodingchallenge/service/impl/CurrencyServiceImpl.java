package com.crewmeister.cmcodingchallenge.service.impl;

import com.crewmeister.cmcodingchallenge.repository.CurrencyRepository;
import com.crewmeister.cmcodingchallenge.service.ICurrencyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 * Implementation of ICurrencyService to manage currency-related operations.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CurrencyServiceImpl implements ICurrencyService {

    private final CurrencyRepository currencyRepository;

    /**
     * Retrieves all currency codes in a paginated and optionally sorted format.
     *
     * @param page    Page number (0-based)
     * @param size    Number of records per page
     * @param sortAsc True for ascending order, false for descending
     * @return Page of currency codes
     */
    @Cacheable("get-all-currencies")
    public Page<String> getAllCurrencies(int page, int size, boolean sortAsc) {
        Sort sort = sortAsc
            ? Sort.by("currencyCode").ascending()
            : Sort.by("currencyCode").descending();

        Pageable sortedByName = PageRequest.of(page, size, sort);
        return currencyRepository.findAllCurrencyCodes(sortedByName);
    }
}