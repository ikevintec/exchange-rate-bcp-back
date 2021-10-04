package com.bcp.reto.exchangerate.service.impl;

import com.bcp.reto.exchangerate.dto.CurrencyRequestDto;
import com.bcp.reto.exchangerate.dto.CurrencyResponseDto;
import com.bcp.reto.exchangerate.entity.Currency;
import com.bcp.reto.exchangerate.enums.ErrorCode;
import com.bcp.reto.exchangerate.exception.ServiceException;
import com.bcp.reto.exchangerate.repository.CurrencyRepository;
import com.bcp.reto.exchangerate.service.CurrencyService;
import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CurrencyServiceImpl implements CurrencyService {

    private final CurrencyRepository repository;

    @Autowired
    public CurrencyServiceImpl(CurrencyRepository repository) {
        this.repository = repository;
    }

    @Override
    public Single<List<CurrencyResponseDto>> findAll() {
        return Single.create(singleSubscriber -> {
            try {
                singleSubscriber.onSuccess(repository.findAllByStatusIsTrue()
                        .stream()
                        .map(this::transformToDto).collect(Collectors.toList())
                );
            } catch (Exception ex) {
                singleSubscriber.onError(new ServiceException(ErrorCode.E000, ex.getMessage()));
            }
        });
    }


    @Override
    public Single<CurrencyResponseDto> findById(Long id) {
        return Single.create(singleSubscriber -> {
            try {
                Optional<Currency> entity = repository.findByIdAndStatusIsTrue(id);
                entity.ifPresentOrElse(value -> singleSubscriber.onSuccess(transformToDto(value)),
                        () -> singleSubscriber.onError(new ServiceException(ErrorCode.E001)));
            } catch (Exception ex) {
                singleSubscriber.onError(new ServiceException(ErrorCode.E000, ex.getMessage()));
            }
        });
    }

    @Override
    public Single<CurrencyResponseDto> save(CurrencyRequestDto dto) {
        return Single.create(singleSubscriber -> {
            try {
                Currency entity = transformToEntity(dto);
                if (entity.getNationalCurrency()
                        && entity.getExchangeRate().compareTo(BigDecimal.ONE) != 0) {
                    throw new ServiceException(ErrorCode.E002);
                }
                if (entity.getNationalCurrency() && repository.findByNationalCurrencyIsTrueAndStatusIsTrue().isPresent()) {
                    throw new ServiceException(ErrorCode.E003);
                }
                if (repository.findByCodeAndStatusIsTrue(entity.getCode()).isPresent()) {
                    throw new ServiceException(ErrorCode.E004);
                }
                entity.setStatus(true);
                CurrencyResponseDto responseDto = transformToDto(repository.save(entity));
                singleSubscriber.onSuccess(responseDto);
            } catch (Exception ex) {
                catchMethod(singleSubscriber, ex);
            }
        });
    }

    @Override
    @Transactional
    public Single<List<CurrencyResponseDto>> saveAll(List<CurrencyRequestDto> dtos) {
        List<Currency> currenciesExist = repository.findAllListByCode(
                dtos.stream().map(CurrencyRequestDto::getCode).collect(Collectors.toList())
        );
        List<Currency> currenciesResult = new ArrayList<>();
        dtos.forEach(dto -> {
            currenciesExist.stream().filter(ce -> ce.getCode().equals(dto.getCode())).findAny().ifPresentOrElse(ce -> {
                ce.setNationalCurrency(dto.getNationalCurrency());
                ce.setName(dto.getName());
                ce.setExchangeRate(dto.getExchangeRate());
                currenciesResult.add(repository.save(ce));

            }, () -> {
                Currency entity = transformToEntity(dto);
                entity.setStatus(true);
                currenciesResult.add(repository.save(entity));
            });
        });

        return Single.create(singleSubscriber -> {
            singleSubscriber.onSuccess(currenciesResult.stream().map(this::transformToDto).collect(Collectors.toList()));
        });

    }

    @Override
    @Transactional
    public Single<CurrencyResponseDto> update(Long id, CurrencyRequestDto dto) {
        return Single.create(singleSubscriber -> {
            try {
                repository.findByIdAndStatusIsTrue(id).ifPresentOrElse(
                        entity -> {
                            if (dto.getNationalCurrency() &&
                                    !entity.getNationalCurrency() &&
                                    repository.findByNationalCurrencyIsTrueAndStatusIsTrue().isPresent()) {
                                throw new ServiceException(ErrorCode.E003);
                            }
                            if (dto.getNationalCurrency() &&
                                    dto.getExchangeRate().compareTo(BigDecimal.ONE) != 0) {
                                throw new ServiceException(ErrorCode.E002);
                            }
                            entity.setCode(dto.getCode());
                            entity.setName(dto.getName());
                            entity.setExchangeRate(dto.getExchangeRate());
                            entity.setNationalCurrency(dto.getNationalCurrency());
                            CurrencyResponseDto responseDto = transformToDto(repository.save(entity));
                            singleSubscriber.onSuccess(responseDto);

                        }, () -> {
                            throw new ServiceException(ErrorCode.E001);
                        });
            } catch (Exception ex) {
                catchMethod(singleSubscriber, ex);
            }
        });
    }

    @Override
    @Transactional
    public Completable delete(Long id) {
        return Completable.create(completableSubscriber -> {
            try {
                repository.findByIdAndStatusIsTrue(id).ifPresentOrElse(
                        entity -> {
                            repository.delete(id);
                            completableSubscriber.onComplete();
                        }, () -> {
                            throw new ServiceException(ErrorCode.E001);
                        }
                );
            } catch (Exception ex) {
                completableSubscriber.onError(new ServiceException(ErrorCode.E000, ex.getMessage()));
            }
        });
    }

    private Currency transformToEntity(CurrencyRequestDto dto) {
        return Currency.builder()
                .code(dto.getCode())
                .name(dto.getName())
                .nationalCurrency(dto.getNationalCurrency())
                .exchangeRate(dto.getExchangeRate())
                .build();
    }


    private CurrencyResponseDto transformToDto(Currency entity) {
        return CurrencyResponseDto.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .name(entity.getName())
                .nationalCurrency(entity.getNationalCurrency())
                .exchangeRate(entity.getExchangeRate())
                .build();
    }

    private void catchMethod(SingleEmitter<CurrencyResponseDto> singleSubscriber, Exception ex) {
        if (ex instanceof ServiceException) {
            singleSubscriber.onError(ex);
        } else {
            singleSubscriber.onError(new ServiceException(ErrorCode.E000, ex.getMessage()));
        }
    }

}




