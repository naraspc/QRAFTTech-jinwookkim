package com.example.QRAFT.stockPriceInfo.service;

import com.example.QRAFT.stockPriceInfo.dto.StockPriceRequestDto;
import com.example.QRAFT.stockPriceInfo.dto.StockPriceResponseDto;
import com.example.QRAFT.stockPriceInfo.entity.StockPriceInfoEntity;
import com.example.QRAFT.stockPriceInfo.repository.StockPriceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
class StockPriceServiceTest {

    @MockBean
    private StockPriceRepository stockPriceRepository;
    @Autowired
    private StockPriceService stockPriceService;

    @BeforeEach
    void setUp() {
        stockPriceRepository = mock(StockPriceRepository.class);
        stockPriceService = new StockPriceService(stockPriceRepository);
    }

    @Test
    @DisplayName("Price타입 요청 테스트코드 ")
    void testGetClosePriceOrDailyReturns_PriceRequest() {
        // Arrange
        StockPriceRequestDto request = new StockPriceRequestDto("AAPL", LocalDate.now().minusDays(10), LocalDate.now(), "PRICE");

        List<StockPriceInfoEntity> mockStockPrices = new ArrayList<>();

        mockStockPrices.add(createMockStockEntity(LocalDate.now().minusDays(10), BigDecimal.valueOf(100)));

        mockStockPrices.add(createMockStockEntity(LocalDate.now().minusDays(9), BigDecimal.valueOf(105)));

        when(stockPriceRepository.findByStockCodeAndDataDateBetween(anyString(), any(), any())).thenReturn(mockStockPrices);

        // Act
        List<StockPriceResponseDto> result = stockPriceService.getClosePriceOrDailyReturns(request);

        // Assert
        assertEquals(2, result.size());
        // Add more assertions if needed
    }

    @Test
    @DisplayName("Return타입 요청 테스트코드")
    void testGetClosePriceOrDailyReturns_ReturnRequest() {
        // Arrange
        StockPriceRequestDto request = new StockPriceRequestDto("AAPL", LocalDate.now().minusDays(10), LocalDate.now(), "RETURN");
        List<StockPriceInfoEntity> mockStockPrices = new ArrayList<>();
        mockStockPrices.add(createMockStockEntity(LocalDate.now().minusDays(10), BigDecimal.valueOf(100)));
        mockStockPrices.add(createMockStockEntity(LocalDate.now().minusDays(9), BigDecimal.valueOf(105)));
        when(stockPriceRepository.findByStockCodeAndDataDateBetween(anyString(), any(), any())).thenReturn(mockStockPrices);

        // Act
        List<StockPriceResponseDto> result = stockPriceService.getClosePriceOrDailyReturns(request);

        // Assert
        assertEquals(1, result.size());
        // Add more assertions if needed
    }

    @Test
    @DisplayName("잘못된 요청 예외처리 테스트")
    void testGetClosePriceOrDailyReturns_InvalidType() {
        // Arrange
        StockPriceRequestDto request = new StockPriceRequestDto("AAPL", LocalDate.now().minusDays(10), LocalDate.now(), "INVALID");

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            stockPriceService.getClosePriceOrDailyReturns(request);
        });
        assertEquals("잘못된 입력 입니다. 입력한 변수 명 : INVALID", exception.getMessage());
    }

    // Helper method to create mock StockEntity
    private StockPriceInfoEntity createMockStockEntity(LocalDate date, BigDecimal price) {
        return StockPriceInfoEntity.builder()
                .stockCode("AAPL")
                .closePrice(price)
                .currency("USD")
                .dataDate(date)
                .build();
    }
}