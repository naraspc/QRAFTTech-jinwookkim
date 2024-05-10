package com.example.QRAFT.portfolio.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PortfolioStock {

    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "portfolio_id", nullable = false)
    private PortfolioEntity portfolio;

    @Column(nullable = false)
    private String stockCode;

    @Column(nullable = false)
    private Integer quantity;



    // 명확한 이름으로 의도와 다른 동작을 방지하기위해 사용
    public void setPortfolioInPortfolioStockEntity(PortfolioEntity portfolio) {
        this.portfolio = portfolio;
    }
}