package com.crewmeister.cmcodingchallenge.external.feign.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ExchangeRateApiResponse(Data data)  {}