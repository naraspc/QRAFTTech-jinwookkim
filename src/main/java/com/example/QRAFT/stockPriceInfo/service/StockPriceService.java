package com.example.QRAFT.stockPriceInfo.service;


import com.example.QRAFT.stockPriceInfo.dto.StockPriceRequestDto;
import com.example.QRAFT.stockPriceInfo.dto.StockPriceResponseDto;
import com.example.QRAFT.stockPriceInfo.entity.StockPriceInfoEntity;
import com.example.QRAFT.stockPriceInfo.repository.StockPriceRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
@RequiredArgsConstructor
public class StockPriceService {

    private final StockPriceRepository stockPriceRepository;

    public List<StockPriceResponseDto> getClosePriceOrDailyReturns(StockPriceRequestDto request) {
        List<StockPriceInfoEntity> stockPrices = stockPriceRepository.findByStockCodeAndDataDateBetween(
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

    public Map<LocalDate, BigDecimal> getStockPrices(String stockCode, LocalDate startDate) {

        List<StockPriceInfoEntity> stockPricesData = stockPriceRepository.findAllByStockCodeAndDataDateFromStart(stockCode, startDate);

        // 일별 종가 map 형태로 가공
        Map<LocalDate, BigDecimal> dailyPrices = new TreeMap<>();
        stockPricesData.forEach(price -> dailyPrices.put(price.getDataDate(), price.getClosePrice()));

        return dailyPrices;
    }

    //더 직관적인 메소드 명은 없을까? 고민 해보자
    private List<StockPriceResponseDto> getClosePriceDataByPriceRequest(List<StockPriceInfoEntity> stockPrices) {
        List<StockPriceResponseDto> priceData = new ArrayList<>();
        stockPrices.forEach(stock -> priceData.add(new StockPriceResponseDto(
                stock.getDataDate(), stock.getClosePrice(), "price"
        )));
        return priceData;
    }


    private List<StockPriceResponseDto> getDailyReturnsDataByReturnRequest(List<StockPriceInfoEntity> stockPrices) {
        List<StockPriceResponseDto> returnRateData = new ArrayList<>();
        if (stockPrices.size() < 2) return returnRateData;

        BigDecimal previousPrice = stockPrices.get(0).getClosePrice();
        for (StockPriceInfoEntity currentStock : stockPrices.subList(1, stockPrices.size())) {
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
