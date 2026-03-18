package com.crewmeister.cmcodingchallenge.service.helper;

import com.crewmeister.cmcodingchallenge.model.Currency;
import com.crewmeister.cmcodingchallenge.model.ExchangeRate;
import com.crewmeister.cmcodingchallenge.repository.CurrencyRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExchangeRateHelperService {
    private final CurrencyRepository currencyRepository;
    public List<Map<String, Map<String, Double>>> formatResult(
        List <ExchangeRate> input){
        List<String> currencies = currencyRepository.findAll().stream().map(Currency::getCurrencyCode).toList();
        Map<String, Map<String, Double>> currencyDataMap = new TreeMap<>();
        List<Map<String, Map<String, Double>>> finalFormattedResult = new ArrayList<>();
        List<Map<String, Map<String, Double>>> formattedInput = input.stream().map(this::formatToMap).toList();
        for (String currency: currencies){
            Map<String, Double> dateRateMap = new TreeMap<>();
            for(Map<String, Map<String, Double>> element: formattedInput){
                if (element.containsKey(currency)){
                    Map<String, Double> currencyData = element.get(currency);
                    String date = currencyData.keySet().iterator().next();
                    Double rate = currencyData.get(date);
                    dateRateMap.put(date, rate);
                }
            }
            currencyDataMap.put(currency, dateRateMap);

        }
        finalFormattedResult.add(currencyDataMap);
        return finalFormattedResult;
    }

    // Format one currency/date/rate data received from DB to Map
    public Map<String, Map<String, Double>> formatToMap(ExchangeRate currencyExchangeRate){
        Map<String, Double> erByDate = new HashMap<>();
        erByDate.put(currencyExchangeRate.getDate().toString(), currencyExchangeRate.getExchangeRate());
        Map<String, Map<String, Double>> result = new HashMap<>();
        result.put(currencyExchangeRate.getCurrency().getCurrencyCode(), erByDate);
        return result;
    }
}
