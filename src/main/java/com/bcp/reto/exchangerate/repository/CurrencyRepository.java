package com.bcp.reto.exchangerate.repository;

import com.bcp.reto.exchangerate.entity.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency, Long> {


    List<Currency> findAllByStatusIsTrue();

    @Query(value = "SELECT p FROM Currency p " +
            " where (p.nationalCurrency = true or p.code = :originCode or p.code = :targetCode) " +
            " and p.status = true " +
            " order by p.nationalCurrency desc"
    )
    List<Currency> findAllForExchange(@Param("originCode") String originCode,
                                      @Param("targetCode") String targetCode);


    Optional<Currency> findByIdAndStatusIsTrue(Long id);

    Optional<Currency> findByCodeAndStatusIsTrue(String code);

    Optional<Currency> findByNationalCurrencyIsTrueAndStatusIsTrue();

    @Modifying
    @Transactional
    @Query("update Currency t set t.status=false where t.id=:id")
    void delete(@Param("id") Long id);

}
