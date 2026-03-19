package com.crewmeister.cmcodingchallenge.dto;

import java.math.BigDecimal;
import java.math.RoundingMode;

public record CurrencyAndRateKeyValue(String currency, Double rate) {

    public CurrencyAndRateKeyValue {
        if (rate != null) {
            rate = BigDecimal.valueOf(rate)
                .setScale(3, RoundingMode.HALF_UP)
                .doubleValue();
        }
    }
}
