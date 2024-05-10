package com.example.QRAFT.portfolio.repository;

import com.example.QRAFT.portfolio.entity.PortfolioStock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PortfolioStockRepository extends JpaRepository<PortfolioStock, UUID> {
}
