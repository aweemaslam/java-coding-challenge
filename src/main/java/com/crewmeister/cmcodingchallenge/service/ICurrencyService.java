package com.crewmeister.cmcodingchallenge.service;

import org.springframework.data.domain.Page;

public interface ICurrencyService {
     Page<String> getAllCurrencies(int page, int size, boolean sortAsc);
}
