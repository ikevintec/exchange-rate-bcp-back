/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bcp.reto.exchangerate.service;

import com.bcp.reto.exchangerate.dto.ExchangeRateRequestDto;
import com.bcp.reto.exchangerate.dto.ExchangeRateResponseDto;
import io.reactivex.Single;

public interface ExchangeRateService {

    Single<ExchangeRateResponseDto> exchange(ExchangeRateRequestDto requestDto);

}
