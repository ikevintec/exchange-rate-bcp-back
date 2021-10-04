package com.bcp.reto.exchangerate.controller;

import com.bcp.reto.exchangerate.dto.ExchangeRateRequestDto;
import com.bcp.reto.exchangerate.dto.ExchangeRateResponseDto;
import com.bcp.reto.exchangerate.service.ExchangeRateService;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
    @RequestMapping(value = "/api/exchange-rates")
public class ExchangeRateRestController {

    private final ExchangeRateService exchangeRateService;

    @Autowired
    public ExchangeRateRestController(ExchangeRateService exchangeRateService) {
        this.exchangeRateService = exchangeRateService;
    }


    @PostMapping
    public Single<ResponseEntity<ExchangeRateResponseDto>> exchange(
            @RequestBody ExchangeRateRequestDto exchangeRateRequestDto) {
        return exchangeRateService.exchange(exchangeRateRequestDto).subscribeOn(Schedulers.io())
                .map(ResponseEntity::ok);
    }

}
