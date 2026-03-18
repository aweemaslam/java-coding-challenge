package com.crewmeister.cmcodingchallenge.external.feign.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Series(
    Map<String, List<Object>> observations
) {

}
