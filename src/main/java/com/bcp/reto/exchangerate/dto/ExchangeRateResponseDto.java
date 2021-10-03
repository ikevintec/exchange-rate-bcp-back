package com.bcp.reto.exchangerate.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExchangeRateResponseDto implements Serializable {
    private String sourceCurrencyCode;
    private String targetCurrencyCode;
    private BigDecimal amount;
    private BigDecimal exchangeRate;
    private BigDecimal amountExchangeRate;
}
