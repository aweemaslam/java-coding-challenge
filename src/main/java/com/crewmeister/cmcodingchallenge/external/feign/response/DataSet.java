package com.crewmeister.cmcodingchallenge.external.feign.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DataSet(
        String action,
        String validFrom,
        Map<String, Series> series
) {}