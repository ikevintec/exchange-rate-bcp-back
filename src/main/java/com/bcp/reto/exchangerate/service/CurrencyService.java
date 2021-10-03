/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bcp.reto.exchangerate.service;

import com.bcp.reto.exchangerate.dto.CurrencyRequestDto;
import com.bcp.reto.exchangerate.dto.CurrencyResponseDto;
import com.bcp.reto.exchangerate.entity.Currency;
import io.reactivex.Completable;
import io.reactivex.Single;

import java.util.List;

public interface CurrencyService {

    Single<List<CurrencyResponseDto>> findAll();

    Single<CurrencyResponseDto> findById(Long id);

    Single<CurrencyResponseDto> save(CurrencyRequestDto dto);

    Single<CurrencyResponseDto> update(Long id, CurrencyRequestDto model);

    Completable delete(Long id);
}
