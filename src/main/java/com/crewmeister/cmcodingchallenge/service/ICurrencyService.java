package com.crewmeister.cmcodingchallenge.service;

import org.springframework.data.domain.Page;

/**
 * Service interface for managing currency-related operations.
 */
public interface ICurrencyService {

     /**
      * Retrieves all currency codes in a paginated and optionally sorted format.
      *
      * @param page    Page number (0-based)
      * @param size    Number of records per page
      * @param sortAsc True to sort currency codes ascending, false for descending
      * @return Page containing currency codes
      */
     Page<String> getAllCurrencies(int page, int size, boolean sortAsc);
}