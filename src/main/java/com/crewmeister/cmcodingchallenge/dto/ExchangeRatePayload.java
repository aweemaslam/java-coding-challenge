package com.crewmeister.cmcodingchallenge.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record ExchangeRatePayload(@NotBlank(message = "Currency code is required")
                                  @Size(min = 3, max = 3, message = "Currency code must be 3 letters")
                                  @Pattern(regexp = "^[A-Z]{3}$", message = "Currency must be 3 uppercase letters")
                                  String currencyCode,

                                  @NotNull(message = "Date is required")
                                  @JsonFormat(pattern = "yyyy-MM-dd")
                                  LocalDate date,

                                  @NotNull(message = "Exchange amount is required")
                                  @Positive
                                  Double exchangeAmount
) {

}
