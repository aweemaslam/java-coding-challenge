package com.crewmeister.cmcodingchallenge.controller;

import com.crewmeister.cmcodingchallenge.service.ICurrencyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for currency-related endpoints.
 * Provides operations to fetch available currencies.
 */
@Slf4j
@RestController
@RequestMapping("/api/currency")
@RequiredArgsConstructor
@Tag(name = "Currency APIs", description = "Operations to fetch available currencies")
public class CurrencyController {

    private final ICurrencyService currencyService;

    /**
     * Get all available currencies, paginated and optionally sorted.
     *
     * @param page    Page number for pagination (default 0)
     * @param size    Page size (default 10)
     * @param sortAsc Sort alphabetically ascending if true, descending if false (default true)
     * @return Paginated list of currency codes
     */
    @GetMapping
    @Operation(summary = "Get all currencies", description = "Fetch all available currency codes, paginated and optionally sorted")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved list of currencies"),
        @ApiResponse(responseCode = "404", description = "No currencies found")
    })
    public ResponseEntity<Page<String>> getCurrencies(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "true") boolean sortAsc) {

        log.info("GET /api/currency - Fetching all available currencies");

        Page<String> allCurrencies = currencyService.getAllCurrencies(page, size, sortAsc);

        if (allCurrencies.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Page.empty());
        }

        log.info("Total {} currencies, returning {} currencies for current page", allCurrencies.getTotalElements(), allCurrencies.getNumberOfElements());
        return ResponseEntity.ok(allCurrencies);
    }
}