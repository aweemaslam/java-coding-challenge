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

@Service
@RequiredArgsConstructor
@Slf4j
public class CurrencyServiceImpl implements ICurrencyService {

    private final CurrencyRepository currencyRepository;

    @Cacheable("get-all-currencies")
    public Page<String> getAllCurrencies(int page, int size, boolean sortAsc) {
        Pageable sortedByName;
        if(sortAsc) {
             sortedByName =
                PageRequest.of(page, size, Sort.by("currencyCode").ascending());
        } else {
             sortedByName =
                PageRequest.of(page, size, Sort.by("currencyCode").descending());
        }
        log.error("xxxxxxx");
        return currencyRepository.findAllCurrencyCodes(sortedByName);
    }
}