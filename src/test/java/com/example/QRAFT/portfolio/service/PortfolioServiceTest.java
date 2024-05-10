package com.example.QRAFT.portfolio.service;

import com.example.QRAFT.portfolio.dto.PortfolioRequestDto;
import com.example.QRAFT.portfolio.dto.PortfolioResponseDto;
import com.example.QRAFT.portfolio.entity.PortfolioEntity;
import com.example.QRAFT.portfolio.entity.PortfolioStock;
import com.example.QRAFT.portfolio.repository.PortfolioRepository;
import com.example.QRAFT.stockPriceInfo.service.StockPriceService;
import com.example.QRAFT.user.entity.UserEntity;
import com.example.QRAFT.user.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PortfolioServiceTest {

    @Mock
    private PortfolioRepository portfolioRepository;

    @Mock
    private UserService userService;

    @Mock
    private StockPriceService stockPriceService;

    @InjectMocks
    private PortfolioService portfolioService;

    private UUID userId;
    private UserEntity userEntity;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        userEntity = UserEntity.builder()
                .id(userId)
                .userName("testUser")
                .registerDate(LocalDate.now())
                .build();
    }

    @Test
    @DisplayName("포트폴리오 생성 테스트")
    void createPortfolio_ShouldSavePortfolio() {
        // Given
        PortfolioRequestDto requestDto = new PortfolioRequestDto(List.of(
                new PortfolioRequestDto.StockEntryDto("AAPL", 100),
                new PortfolioRequestDto.StockEntryDto("GOOGL", 150)
        ));
        when(userService.findUserEntity(userId)).thenReturn(userEntity);
        when(portfolioRepository.save(any(PortfolioEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        portfolioService.createPortfolioEntityWithStock(requestDto, userId);

        // Then
        verify(portfolioRepository).save(any(PortfolioEntity.class));
        verify(userService).findUserEntity(userId);
    }
    @Test
    @DisplayName("포트폴리오에 주식 데이터 추가")
    void addPersonalStockDataInPortfolio_Success() {
        // Given
        UUID portfolioId = UUID.randomUUID();
        PortfolioRequestDto.StockEntryDto stockEntryDto = new PortfolioRequestDto.StockEntryDto("MSFT", 50);
        PortfolioEntity portfolio = PortfolioEntity.builder()
                .id(portfolioId)
                .stocks(new ArrayList<>()) // 초기 주식 목록은 비어있습니다.
                .build();

        when(portfolioRepository.findById(portfolioId)).thenReturn(Optional.of(portfolio));

        // When
        portfolioService.addPersonalStockDataInPortfolio(stockEntryDto, portfolioId);

        // Then
        verify(portfolioRepository).findById(portfolioId);
        assertEquals(1, portfolio.getStocks().size()); // 확인: 주식 목록에 하나의 주식이 추가되었어야 합니다.
        PortfolioStock addedStock = portfolio.getStocks().get(0);
        assertEquals("MSFT", addedStock.getStockCode());
        assertEquals(50, addedStock.getQuantity());
    }

    @Test
    @DisplayName("포트폴리오에 주식 데이터 추가 - 실패, 포트폴리오 미존재")
    void addPersonalStockDataInPortfolio_Failure() {
        // Given
        UUID portfolioId = UUID.randomUUID();
        PortfolioRequestDto.StockEntryDto stockEntryDto = new PortfolioRequestDto.StockEntryDto("MSFT", 50);

        when(portfolioRepository.findById(portfolioId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> {
            portfolioService.addPersonalStockDataInPortfolio(stockEntryDto, portfolioId);
        });
    }
    @Test
    @DisplayName("포트폴리오 삭제 - 성공적인 삭제")
    void deletePortfolio_Success() {
        //Given
        UUID portfolioId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        PortfolioEntity portfolio = PortfolioEntity.builder()
                .id(portfolioId)
                .userEntity(UserEntity.builder().id(userId).build())
                .build();

        when(portfolioRepository.findById(portfolioId)).thenReturn(Optional.of(portfolio));

        //When & Then
        portfolioService.deletePortfolio(portfolioId.toString(), userId);
        verify(portfolioRepository).delete(portfolio);
    }

    @Test
    @DisplayName("포트폴리오 삭제 - 포트폴리오 미존재")
    void deletePortfolio_NotFound() {
        //Given
        UUID portfolioId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();


        when(portfolioRepository.findById(portfolioId)).thenReturn(Optional.empty());

        //When & Then
        assertThrows(EntityNotFoundException.class, () ->
                portfolioService.deletePortfolio(portfolioId.toString(), userId)
        );
    }

    @Test
    @DisplayName("포트폴리오 삭제 - 유저 권한 미부합")
    void deletePortfolio_UnauthorizedUser() {
       //Given
        UUID portfolioId = UUID.randomUUID();
        UUID wrongUserId = UUID.randomUUID();
        UUID ownerId = UUID.randomUUID();
        PortfolioEntity portfolio = PortfolioEntity.builder()
                .id(portfolioId)
                .userEntity(UserEntity.builder().id(ownerId).build())
                .build();

        when(portfolioRepository.findById(portfolioId)).thenReturn(Optional.empty());

        //When & Then
        assertThrows(EntityNotFoundException.class, () ->
                portfolioService.deletePortfolio(portfolioId.toString(), wrongUserId)
        );
    }
    @Test
    @DisplayName("Successfully find portfolio and calculate daily values")
    void testFindPortfolio_Success() {
        // Given
        String portfolioId = UUID.randomUUID().toString();
        UUID userId = UUID.randomUUID();
        UserEntity user = UserEntity.builder()
                .id(userId)
                .build();
        PortfolioEntity portfolio = PortfolioEntity.builder()
                .userEntity(user)
                .createdAt(LocalDate.now())
                .stocks(new ArrayList<>())
                .build();

        PortfolioStock stock = PortfolioStock.builder()
                .stockCode("AAPL")
                .quantity(100)
                .portfolio(portfolio)
                .build();
        portfolio.getStocks().add(stock);

        LocalDate startDate = portfolio.getCreatedAt();
        Map<LocalDate, BigDecimal> stockPrices = new TreeMap<>();
        stockPrices.put(startDate, new BigDecimal("150.00"));

        when(portfolioRepository.findById(UUID.fromString(portfolioId)))
                .thenReturn(Optional.of(portfolio));
        when(stockPriceService.getStockPrices(anyString(), any(LocalDate.class)))
                .thenReturn(stockPrices);

        // When
        PortfolioResponseDto result = portfolioService.findPortfolio(portfolioId, userId);

        // Then
        assertNotNull(result);
        assertEquals(new BigDecimal("15000.00"), result.totalValues().get(startDate));
    }

    @Test
    @DisplayName("Portfolio not found or unauthorized access")
    void testFindPortfolio_NotFoundOrUnauthorized() {
        // Given
        String portfolioId = UUID.randomUUID().toString();
        UUID userId = UUID.randomUUID();

        when(portfolioRepository.findById(UUID.fromString(portfolioId)))
                .thenReturn(Optional.empty());

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> {
            portfolioService.findPortfolio(portfolioId, userId);
        });
    }


}