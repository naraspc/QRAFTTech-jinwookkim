package com.example.QRAFT.stockPriceInfo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "STOCK_PRICE_INFO")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockPriceInfoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //요구사항 길이 255자
    @Column(nullable = false, length = 255)
    private String stockCode;

    @Column(nullable = false)
    private BigDecimal closePrice;

    //요구사항 길이 26자
    @Column(nullable = false, length = 26)
    private String currency;

    @Column(nullable = false)
    private LocalDate dataDate;


}
