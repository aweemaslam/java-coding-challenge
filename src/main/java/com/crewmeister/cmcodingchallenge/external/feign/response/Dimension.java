package com.crewmeister.cmcodingchallenge.external.feign.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Dimension(
        String id,
        List<DimensionValue> values
) {}