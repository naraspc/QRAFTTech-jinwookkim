package com.example.QRAFT.stockPriceInfo.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record StockPriceResponseDto(
        LocalDate date,
        BigDecimal value,
        String type
) {

}
