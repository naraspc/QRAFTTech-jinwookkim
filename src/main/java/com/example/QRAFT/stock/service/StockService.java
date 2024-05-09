package com.example.QRAFT.stock.service;


import com.example.QRAFT.stock.dto.StockPriceRequestDto;
import com.example.QRAFT.stock.dto.StockPriceResponseDto;
import com.example.QRAFT.stock.entity.StockEntity;
import com.example.QRAFT.stock.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StockService {

    private final StockRepository stockRepository;

    public List<StockPriceResponseDto> getClosePriceOrDailyReturns(StockPriceRequestDto request) {
        List<StockEntity> stockPrices = stockRepository.findByStockCodeAndDataDateBetween(
                request.stockCode(), request.startDate(), request.endDate()
        );

        if (stockPrices == null) {
            throw new NullPointerException("stockPrices is null");
        }
        return switch (request.type().toUpperCase()) {
            case "PRICE" -> getClosePriceDataByPriceRequest(stockPrices);
            case "RETURN" -> getDailyReturnsDataByReturnRequest(stockPrices);
            default -> throw new IllegalArgumentException("잘못된 입력 입니다. 입력한 변수 명 : " + request.type());
        };

    }

    //더 직관적인 메소드 명은 없을까? 고민 해보자
    private List<StockPriceResponseDto> getClosePriceDataByPriceRequest(List<StockEntity> stockPrices) {
        List<StockPriceResponseDto> priceData = new ArrayList<>();
        stockPrices.forEach(stock -> priceData.add(new StockPriceResponseDto(
                stock.getDataDate(), stock.getClosePrice(), "price"
        )));
        return priceData;
    }


    private List<StockPriceResponseDto> getDailyReturnsDataByReturnRequest(List<StockEntity> stockPrices) {
        List<StockPriceResponseDto> returnRateData = new ArrayList<>();
        if (stockPrices.size() < 2) return returnRateData;

        BigDecimal previousPrice = stockPrices.get(0).getClosePrice();
        for (StockEntity currentStock : stockPrices.subList(1, stockPrices.size())) {
            BigDecimal returnRate = calculateDailyStockReturns(previousPrice, currentStock.getClosePrice());
            returnRateData.add(new StockPriceResponseDto(currentStock.getDataDate(), returnRate, "return"));
            previousPrice = currentStock.getClosePrice();
        }
        return returnRateData;
    }

    private BigDecimal calculateDailyStockReturns(BigDecimal previousPrice, BigDecimal currentPrice) {
        return currentPrice.subtract(previousPrice)
                .divide(previousPrice, 4, RoundingMode.HALF_UP);
    }

}
