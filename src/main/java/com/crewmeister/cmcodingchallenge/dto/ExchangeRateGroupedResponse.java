package com.crewmeister.cmcodingchallenge.dto;

import java.util.List;

public record ExchangeRateGroupedResponse(String date, List<CurrencyAndRateKeyValue> rates) {

}
