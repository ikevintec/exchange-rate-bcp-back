package com.bcp.reto.exchangerate.entity;


import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class Currency implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 10)
    private String code;

    @Column(length = 10)
    private String name;

    @Column
    private Boolean nationalCurrency;

    @Column(precision = 12, scale = 4)
    private BigDecimal exchangeRate;

    @Column
    private Boolean status;


}
