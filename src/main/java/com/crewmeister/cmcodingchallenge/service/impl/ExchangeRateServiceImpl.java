package com.crewmeister.cmcodingchallenge.service.impl;

import com.crewmeister.cmcodingchallenge.service.IExchangeRateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ExchangeRateServiceImpl implements IExchangeRateService {

   /* private final ExchangeRateRepository exchangeRateRepository;
    private final ExchangeRateHelperService exchangeRateHelperService;
    @Cacheable("exchangeRates")
    @Override
    public Page<ExchangeRateResponse> getAllExchangeRates(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        //ExchangeRateResponse formattedResponse =  exchangeRateHelperService.formatResult(exchangeRateRepository.findAll());
        return  null;
    }*/
}