package com.bcp.reto.exchangerate.service.impl;

import com.bcp.reto.exchangerate.dto.ExchangeRateRequestDto;
import com.bcp.reto.exchangerate.dto.ExchangeRateResponseDto;
import com.bcp.reto.exchangerate.entity.Currency;
import com.bcp.reto.exchangerate.enums.ErrorCode;
import com.bcp.reto.exchangerate.exception.ServiceException;
import com.bcp.reto.exchangerate.repository.CurrencyRepository;
import com.bcp.reto.exchangerate.service.ExchangeRateService;
import io.reactivex.Single;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class ExchangeRateServiceImpl implements ExchangeRateService {

    private final CurrencyRepository currencyRepository;

    @Value("${app.number-decimals}")
    private Integer numberDecimals;

    @Autowired
    public ExchangeRateServiceImpl(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    @Override
    public Single<ExchangeRateResponseDto> exchange(ExchangeRateRequestDto requestDto) {
        return Single.create(singleSubscriber -> {
            try {
                List<Currency> currencies = currencyRepository
                        .findAllForExchange(requestDto.getSourceCurrencyCode(), requestDto.getTargetCurrencyCode());
                currencies.stream()
                        .filter(Currency::getNationalCurrency)
                        .findFirst()
                        .orElseThrow(() -> new ServiceException(ErrorCode.E005));
                Currency sourceCurrency = currencies.stream()
                        .filter(c -> c.getCode().equalsIgnoreCase(requestDto.getSourceCurrencyCode()) && c.getExchangeRate()!=null)
                        .findFirst()
                        .orElseThrow(() -> new ServiceException(ErrorCode.E006, requestDto.getSourceCurrencyCode()));
                Currency targetCurrency = currencies.stream()
                        .filter(c -> c.getCode().equalsIgnoreCase(requestDto.getTargetCurrencyCode()) && c.getExchangeRate()!=null)
                        .findFirst()
                        .orElseThrow(() -> new ServiceException(ErrorCode.E007, requestDto.getTargetCurrencyCode()));

                BigDecimal amountNationalCurrency = requestDto.getAmount().multiply(sourceCurrency.getExchangeRate());
                BigDecimal amountExchangeRate = amountNationalCurrency
                        .divide(targetCurrency.getExchangeRate(), numberDecimals, RoundingMode.HALF_UP);
                singleSubscriber.onSuccess(ExchangeRateResponseDto.builder()
                        .amount(requestDto.getAmount())
                        .amountExchangeRate(amountExchangeRate)
                        .exchangeRate(!targetCurrency.getNationalCurrency() ? targetCurrency.getExchangeRate() : sourceCurrency.getExchangeRate())
                        .sourceCurrencyCode(requestDto.getSourceCurrencyCode())
                        .targetCurrencyCode(requestDto.getTargetCurrencyCode())
                        .build());
            } catch (Exception ex) {
                if (ex instanceof ServiceException) {
                    singleSubscriber.onError(ex);
                } else {
                    singleSubscriber.onError(new ServiceException(ErrorCode.E000, ex.getMessage()));
                }
            }
        });
    }
}




