package com.example.QRAFT.portfolio.service;

import com.example.QRAFT.portfolio.dto.PortfolioRequestDto;
import com.example.QRAFT.portfolio.entity.PortfolioEntity;
import com.example.QRAFT.portfolio.repository.PortfolioRepository;
import com.example.QRAFT.user.entity.UserEntity;
import com.example.QRAFT.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PortfolioServiceTest {

    @Mock
    private PortfolioRepository portfolioRepository;

    @Mock
    private UserService userService;

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
        portfolioService.createPortfolio(requestDto, userId);

        // Then
        verify(portfolioRepository).save(any(PortfolioEntity.class));
        verify(userService).findUserEntity(userId);
    }
}