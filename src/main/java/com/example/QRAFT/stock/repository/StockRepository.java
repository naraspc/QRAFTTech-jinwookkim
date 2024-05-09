package com.example.QRAFT.stock.repository;

import com.example.QRAFT.stock.entity.StockPriceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockRepository extends JpaRepository<StockPriceEntity, Long> {
}
