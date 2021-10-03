package com.bcp.reto.exchangerate.controller;

import com.bcp.reto.exchangerate.dto.CurrencyRequestDto;
import com.bcp.reto.exchangerate.dto.CurrencyResponseDto;
import com.bcp.reto.exchangerate.service.CurrencyService;
import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping(value = "/api/currencies")
public class CurrencyRestController {

    private final CurrencyService currencyService;

    @Autowired
    public CurrencyRestController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @PostMapping
    public Single<ResponseEntity<CurrencyResponseDto>> save(
        @RequestBody CurrencyRequestDto currencyRequestDto) {
        return currencyService.save(currencyRequestDto).subscribeOn(Schedulers.io())
                .map(ResponseEntity::ok);
    }

    @PutMapping(value = "/{id}")
    public Single<ResponseEntity<CurrencyResponseDto>> update(@PathVariable(value = "id") Long id,
                                                              @RequestBody CurrencyRequestDto currencyRequestDto) {
        return currencyService.update(id, currencyRequestDto)
                .subscribeOn(Schedulers.io())
                .map(ResponseEntity::ok);
    }


    @GetMapping
    public Single<ResponseEntity<List<CurrencyResponseDto>>> findAll() {
        return currencyService.findAll()
                .subscribeOn(Schedulers.io())
                .map(ResponseEntity::ok);
    }

    @GetMapping(value = "/{id}")
    public Single<ResponseEntity<CurrencyResponseDto>> findById(@PathVariable(value = "id") Long id) {
        return currencyService.findById(id)
                .subscribeOn(Schedulers.io())
                .map(ResponseEntity::ok);
    }

    @DeleteMapping(value = "/{id}")
    public Completable deleteBook(@PathVariable(value = "id") Long id) {
        return currencyService.delete(id)
                .subscribeOn(Schedulers.io());
    }

}
