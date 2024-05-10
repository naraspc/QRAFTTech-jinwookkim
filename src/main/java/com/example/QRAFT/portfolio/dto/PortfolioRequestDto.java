package com.example.QRAFT.portfolio.dto;

import java.util.List;

public record PortfolioRequestDto(

        List<StockEntryDto> stockEntries

) {
    public record StockEntryDto(

            String stockCode, int quantity

    ) {
    }
}