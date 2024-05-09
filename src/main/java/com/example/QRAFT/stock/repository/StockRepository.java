package com.example.QRAFT.stock.repository;

import com.example.QRAFT.stock.entity.StockEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface StockRepository extends JpaRepository<StockEntity, Long> {
    List<StockEntity> findByStockCodeAndDataDateBetween(String stockCode, LocalDate startDate, LocalDate endDate);

}
