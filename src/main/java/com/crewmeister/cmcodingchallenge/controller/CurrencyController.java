package com.crewmeister.cmcodingchallenge.controller;

import com.crewmeister.cmcodingchallenge.service.ICurrencyService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for currency-related endpoints.
 * Currency APis.
 */
@Slf4j
@RestController
@RequestMapping("/api/currency")
@RequiredArgsConstructor
@Tag(name = "Currency API")
public class CurrencyController {

    private final ICurrencyService currencyService;

    /**
     * Get all available currencies.
     *
     * @return List of all currency names
     */
    @GetMapping
    public ResponseEntity<Page<String>> getCurrencies(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "true") boolean sortAsc) {
        log.info("GET /api/currency - Fetching all available currencies");

        Page<String> allCurrencies = currencyService.getAllCurrencies(page, size, sortAsc);
        if (allCurrencies.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Page.empty());
        }
        log.info("Returning {} currencies", allCurrencies.getTotalElements());
        return ResponseEntity.ok(allCurrencies);
    }

}