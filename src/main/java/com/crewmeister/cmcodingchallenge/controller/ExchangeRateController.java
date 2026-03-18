package com.crewmeister.cmcodingchallenge.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for exchange rate operations.
 * Provides endpoints for querying EUR-FX exchange rates and currency conversion.
 */
@RestController
@RequestMapping("/api/exchange-rates")
@Tag(name = "Exchange rate related operation APIs")
@Slf4j
@RequiredArgsConstructor
public class ExchangeRateController {


   /* private final IExchangeRateService exchangeRateService;


    *//**
     * Get all available EUR-FX exchange rates.
     * Returns exchange rates for all currencies at all available dates.
     * 
     * @return List of all exchange rates sorted by date (most recent first)
     *//*
    @GetMapping
    public ResponseEntity<Page<ExchangeRateResponse>> getAllExchangeRates(@RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size) {
        log.info("GET /api/exchange-rates - Fetching all exchange rates");

        Page<ExchangeRateResponse> rates = exchangeRateService.getAllExchangeRates( page,  size);
        if (rates.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Page.empty());
        }
        log.info("Returning {} exchange rate records", rates.getTotalElements());
        return ResponseEntity.ok(rates);
    }

    *//**
     * Get EUR-FX exchange rates for a specific date.
     * Returns exchange rates for all currencies on the given date.
     * 
     * @param date The date to get exchange rates for (ISO format: yyyy-MM-dd)
     * @return List of exchange rates for the specified date
     *//*
    @GetMapping("/date/{date}")
    public ResponseEntity<List<ExchangeRateDto>> getExchangeRatesByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        
        logger.info("GET /api/exchange-rates/date/{} - Fetching rates for date", date);
        
        List<ExchangeRateDto> rates = exchangeRateService.getExchangeRatesByDate(date);
        
        logger.info("Returning {} exchange rates for date {}", rates.size(), date);
        return ResponseEntity.ok(rates);
    }

    *//**
     * Get EUR-FX exchange rate for a specific currency on a specific date.
     * 
     * @param currencyCode ISO currency code (e.g., USD, GBP, JPY)
     * @param date The date to get the exchange rate for (ISO format: yyyy-MM-dd)
     * @return Exchange rate information for the specified currency and date
     *//*
    @GetMapping("/{currencyCode}/{date}")
    public ResponseEntity<ExchangeRateDto> getExchangeRate(
            @PathVariable String currencyCode,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        
        logger.info("GET /api/exchange-rates/{}/{} - Fetching specific rate", currencyCode, date);
        
        ExchangeRateDto rate = exchangeRateService.getExchangeRate(currencyCode, date);
        
        return ResponseEntity.ok(rate);
    }

    *//**
     * Convert a foreign currency amount to EUR.
     * Uses the exchange rate for the specified date.
     * 
     * @param currencyCode Source currency code (ISO format)
     * @param amount Amount in the source currency to convert
     * @param date Date for the exchange rate to use (ISO format: yyyy-MM-dd)
     * @return Conversion result including the EUR amount and rate used
     *//*
    @GetMapping("/convert")
    public ResponseEntity<ConversionResultDto> convertToEur(
            @RequestParam String currencyCode,
            @RequestParam BigDecimal amount,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        
        logger.info("GET /api/exchange-rates/convert - Converting {} {} to EUR for date {}", 
                amount, currencyCode, date);
        
        ConversionResultDto result = exchangeRateService.convertToEur(currencyCode, amount, date);
        
        logger.info("Conversion result: {} {} = {} EUR", 
                result.getSourceAmount(), result.getSourceCurrency(), result.getConvertedAmount());
        
        return ResponseEntity.ok(result);
    }*/
}

