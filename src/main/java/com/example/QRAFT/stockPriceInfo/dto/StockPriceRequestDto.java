package com.example.QRAFT.stockPriceInfo.dto;

import java.time.LocalDate;

public record StockPriceRequestDto(
        String stockCode,
        LocalDate startDate,
        LocalDate endDate,
        String type
) {
}