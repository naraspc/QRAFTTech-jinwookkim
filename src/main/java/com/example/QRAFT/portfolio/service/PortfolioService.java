package com.example.QRAFT.portfolio.service;


import com.example.QRAFT.portfolio.dto.PortfolioRequestDto;
import com.example.QRAFT.portfolio.dto.PortfolioResponseDto;
import com.example.QRAFT.portfolio.entity.PortfolioEntity;
import com.example.QRAFT.portfolio.entity.PortfolioStock;
import com.example.QRAFT.portfolio.repository.PortfolioRepository;
import com.example.QRAFT.stockPriceInfo.service.StockPriceService;
import com.example.QRAFT.user.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;

    // userRepository를 직접 의존하지 않기위해 service에 UserEntity 관련 메소드 정의 후 호출
    private final UserService userService;

    // stockRepository를 의존하지않기 위해 선언
    private final StockPriceService stockPriceService;


    //포트폴리오와 포트폴리오 Stock 함께등록
    @Transactional
    public void createPortfolioEntityWithStock(PortfolioRequestDto requestDto, UUID userId) {

        List<PortfolioStock> stocks = requestDto.stockEntries().stream()
                .map(entry -> PortfolioStock.builder()
                        .stockCode(entry.stockCode())
                        .quantity(entry.quantity())
                        .build())
                .collect(Collectors.toList());

        PortfolioEntity portfolio = PortfolioEntity.builder()
                .userEntity(userService.findUserEntity(userId))
                .stocks(stocks)
                .name("New Portfolio")  // 예제에서는 임의의 이름을 설정하고 있습니다. 필요에 따라 조정할 수 있습니다.
                .build();

        // 연관 관계 설정
        stocks.forEach(stock -> stock.setPortfolioInPortfolioStockEntity(portfolio));

        PortfolioEntity savedPortfolio = portfolioRepository.save(portfolio);

    }

    //PortfolioStock데이터만 포트폴리오에 추가하기
    @Transactional
    public void addPersonalStockDataInPortfolio(PortfolioRequestDto.StockEntryDto StockEntryDto, UUID entityId) {
        PortfolioEntity portfolio = portfolioRepository.findById(entityId).orElseThrow(() -> new EntityNotFoundException("cannot find portfolio"));

        PortfolioStock newStock = PortfolioStock.builder()
                .stockCode(StockEntryDto.stockCode())
                .quantity(StockEntryDto.quantity())
                .build();

        // 새로운 주식을 기존 포트폴리오의 주식 목록에 추가
        portfolio.getStocks().add(newStock);
    }

    @Transactional
    public void deletePortfolio(String portfolioId, UUID userId) {

        portfolioRepository.delete(findValidPortfolio(portfolioId, userId));

    }


    @Transactional(readOnly = true)
    public PortfolioResponseDto findPortfolio(String portfolioId, UUID userId) {
        PortfolioEntity portfolio = findValidPortfolio(portfolioId, userId);

        LocalDate startDate = portfolio.getCreatedAt();
        Map<LocalDate, BigDecimal> portfolioDailyValues = new TreeMap<>();

        //각 주식에 대한 일별 가치 총합
        for (PortfolioStock stock : portfolio.getStocks()) {
            Map<LocalDate, BigDecimal> stockPrices = stockPriceService.getStockPrices(stock.getStockCode(), startDate);
            stockPrices.forEach((date, price) -> {
                BigDecimal totalValue = price.multiply(BigDecimal.valueOf(stock.getQuantity()));
                portfolioDailyValues.merge(date, totalValue, BigDecimal::add);
            });
        }

        return PortfolioResponseDto.fromEntity(portfolio,portfolioDailyValues);
    }


    private PortfolioEntity findValidPortfolio(String portfolioId, UUID userId) {

        PortfolioEntity portfolio = portfolioRepository.findById(UUID.fromString(portfolioId)).orElseThrow(() -> new EntityNotFoundException("cannot find portfolio"));

        if (!portfolio.getUserEntity().getId().equals(userId)) {
            throw new IllegalArgumentException("해당 포트폴리오는 다른 사용자의 포트폴리오입니다.");
        }
        return portfolio;
    }

}
