package com.crewmeister.cmcodingchallenge.dto;

import java.time.LocalDate;

public record ExchangeRateDto(
    String currency,
    LocalDate date,
    Double rate
) {

}
