package com.example.QRAFT.portfolio.controller;


import com.example.QRAFT.portfolio.dto.PortfolioRequestDto;
import com.example.QRAFT.portfolio.dto.PortfolioResponseDto;
import com.example.QRAFT.portfolio.service.PortfolioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.TreeMap;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(PortfolioController.class)
class PortfolioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PortfolioService portfolioService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("포트폴리오 생성 컨트롤러 테스트")
    public void createPortfolio_ReturnsSuccessMessage() throws Exception {
        // Arrange
        UUID userId = UUID.randomUUID();
        PortfolioRequestDto requestDto = new PortfolioRequestDto(List.of(
                new PortfolioRequestDto.StockEntryDto("AAPL", 100),
                new PortfolioRequestDto.StockEntryDto("GOOGL", 150)
        ));

        doNothing().when(portfolioService).createPortfolioEntityWithStock(any(PortfolioRequestDto.class), any(UUID.class));

        // Act & Assert
        mockMvc.perform(post("/api/portfolio/create/" + userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(content().string("Portfolio created successfully"));
    }
    @Test
    @DisplayName("이미 존재하는 포트폴리오에 주식 데이터 추가 테스트")
    public void addPersonalStockDataInPortfolio_Success() throws Exception {
        // Given
        UUID portfolioId = UUID.randomUUID();
        PortfolioRequestDto.StockEntryDto stockEntryDto = new PortfolioRequestDto.StockEntryDto("MSFT", 200);

        doNothing().when(portfolioService).addPersonalStockDataInPortfolio(any(PortfolioRequestDto.StockEntryDto.class), any(UUID.class));

        // Act & Assert
        mockMvc.perform(post("/api/portfolio/add/" + portfolioId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(stockEntryDto)))
                .andExpect(status().isOk())
                .andExpect(content().string("Stock added successfully"));
    }
    @Test
    @DisplayName("포트폴리오 삭제 테스트")
    public void deletePortfolio_Success() throws Exception {
        // Given
        UUID userId = UUID.randomUUID();
        String portfolioId = UUID.randomUUID().toString();

        doNothing().when(portfolioService).deletePortfolio(eq(portfolioId), eq(userId));

        // Act & Assert
        mockMvc.perform(post("/api/portfolio/delete/" + userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(portfolioId))
                .andExpect(status().isNoContent()); // Expect HTTP 204 No Content
    }
    @Test
    @DisplayName("포트폴리오 조회")
    public void findPortfolio_Success() throws Exception {
        // Given
        UUID userId = UUID.randomUUID();
        String portfolioId = UUID.randomUUID().toString();
        PortfolioResponseDto responseDto = new PortfolioResponseDto("Test Portfolio", new TreeMap<>(), LocalDate.now());

        when(portfolioService.findPortfolio(eq(portfolioId), eq(userId))).thenReturn(responseDto);

        // Act & Assert
        mockMvc.perform(post("/api/portfolio/find/" + userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(portfolioId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.portfolioName").value(responseDto.portfolioName()));
    }
}