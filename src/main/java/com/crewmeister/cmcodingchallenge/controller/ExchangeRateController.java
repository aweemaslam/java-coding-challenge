package com.crewmeister.cmcodingchallenge.controller;

import com.crewmeister.cmcodingchallenge.dto.ExchangeRateGroupedResponse;
import com.crewmeister.cmcodingchallenge.dto.ExchangeRatePayload;
import com.crewmeister.cmcodingchallenge.dto.RateDto;
import com.crewmeister.cmcodingchallenge.service.IExchangeRateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageImpl;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for exchange rate operations. Provides endpoints for querying EUR-FX exchange rates and currency conversion.
 */
@RestController
@RequestMapping("/api/exchange-rate")
@Tag(name = "Exchange Rate APIs", description = "Operations for fetching and converting EUR-FX exchange rates")
@Slf4j
@RequiredArgsConstructor
public class ExchangeRateController {

    private final IExchangeRateService exchangeRateService;

    /**
     * Get all available EUR-FX exchange rates. Returns exchange rates for all currencies at all available dates, paginated.
     *
     * @param pageNo          Page number for pagination (default 0)
     * @param size            Page size (default 10)
     * @param sortByDateAsc   Sort by date ascending if true, descending if false (default true)
     * @param ignoreNullRates ignore exchange rates which are not populated in api response (default true)
     * @return Paginated list of exchange rates grouped by currency
     */
    @GetMapping
    @Operation(summary = "Get all exchange rates", description = "Fetch all EUR-FX exchange rates, paginated and optionally sorted by date")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved exchange rates"),
        @ApiResponse(responseCode = "404", description = "No exchange rates found")
    })
    public ResponseEntity<PageImpl<ExchangeRateGroupedResponse>> getAllExchangeRates(
        @RequestParam(defaultValue = "0") int pageNo,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "true") boolean sortByDateAsc,
        @RequestParam(defaultValue = "true") boolean ignoreNullRates) {

        log.info("GET /api/exchange-rate - Fetching all exchange rates");

        PageImpl<ExchangeRateGroupedResponse> rates =
            exchangeRateService.getAllExchangeRates(pageNo, size, sortByDateAsc, ignoreNullRates);

        if (rates.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        log.info("Returning {} exchange rate(s)", rates.getTotalElements());
        return ResponseEntity.ok(rates);
    }

    /**
     * Get EUR-FX exchange rates for a specific date.
     *
     * @param date            Date to fetch exchange rates for (ISO format: yyyy-MM-dd)
     * @param ignoreNullRates ignore exchange rates which are not populated in api response (default true)
     * @return Exchange rates for all currencies on the specified date
     */
    @GetMapping("/{date}")
    @Operation(summary = "Get exchange rates by date", description = "Fetch all EUR-FX exchange rates for a specific date")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved exchange rates for the date"),
        @ApiResponse(responseCode = "404", description = "No exchange rates found for the specified date")
    })
    public ResponseEntity<ExchangeRateGroupedResponse> getExchangeRatesByDate(
        @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
        @RequestParam(defaultValue = "true") boolean ignoreNullRates) {

        log.info("GET /api/exchange-rate/{} - Fetching rates for date", date);

        ExchangeRateGroupedResponse rate = exchangeRateService.getExchangeRatesByDate(date, ignoreNullRates);
        if (Objects.isNull(rate)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        log.info("Returning {} exchange rates for date {}", rate, date);
        return ResponseEntity.ok(rate);
    }

    /**
     * Convert an rate in a specific currency to EUR for a specific date.
     *
     * @param payload ExchangeRatePayload containing currency code, rate, and date
     * @return Converted rate in Euro
     */
    @PostMapping("/convert")
    @Operation(summary = "Convert currency to Euro", description = "Convert a given rate in a currency to Euro using the exchange rate on the specified date")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully converted rate"),
        @ApiResponse(responseCode = "400", description = "Invalid request payload or date format"),
        @ApiResponse(responseCode = "404", description = "Exchange rate not found for the given currency and date")
    })
    public ResponseEntity<RateDto> convertToEuro(@Valid @RequestBody ExchangeRatePayload payload) {
        RateDto convertedAmount = exchangeRateService.convertToEuro(payload);
        return ResponseEntity.ok(convertedAmount);
    }

    /**
     * Get EUR-FX exchange rate for a specific currency on a specific date.
     *
     * @param currencyCode ISO currency code (e.g., USD, GBP, JPY)
     * @param date         Date to fetch the exchange rate for (ISO format: yyyy-MM-dd)
     * @return Exchange rate for the specified currency and date
     */
    @GetMapping("/{currencyCode}/{date}")
    @Operation(summary = "Get exchange rate by currency and date", description = "Fetch EUR-FX exchange rate for a specific currency on a specific date")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved exchange rate"),
        @ApiResponse(responseCode = "404", description = "Exchange rate not found for the specified currency and date")
    })
    public ResponseEntity<RateDto> getExchangeRate(
        @PathVariable String currencyCode,
        @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        log.info("GET /api/exchange-rate/{}/{} - Fetching specific rate", currencyCode, date);

        RateDto rate = exchangeRateService.getExchangeRateByCurrencyAndDate(currencyCode, date);
        return ResponseEntity.ok(rate);
    }
}