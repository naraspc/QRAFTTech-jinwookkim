package com.example.QRAFT.stockPriceInfo.repository;

import com.example.QRAFT.stockPriceInfo.entity.StockPriceInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface StockPriceRepository extends JpaRepository<StockPriceInfoEntity, Long> {
    List<StockPriceInfoEntity> findByStockCodeAndDataDateBetween(String stockCode, LocalDate startDate, LocalDate endDate);
    @Query("SELECT spi FROM StockPriceInfoEntity spi WHERE spi.stockCode = :stockCode AND spi.dataDate >= :startDate ORDER BY spi.dataDate ASC")
    List<StockPriceInfoEntity> findAllByStockCodeAndDataDateFromStart(@Param("stockCode") String stockCode, @Param("startDate") LocalDate startDate);
}
