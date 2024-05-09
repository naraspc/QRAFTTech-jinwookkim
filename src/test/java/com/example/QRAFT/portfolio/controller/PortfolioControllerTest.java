package com.example.QRAFT.portfolio.controller;


import com.example.QRAFT.portfolio.dto.PortfolioRequestDto;
import com.example.QRAFT.portfolio.dto.PortfolioRequestDto.StockEntryDto;
import com.example.QRAFT.portfolio.service.PortfolioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

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

        doNothing().when(portfolioService).createPortfolio(any(PortfolioRequestDto.class), any(UUID.class));

        // Act & Assert
        mockMvc.perform(post("/api/portfolio/create/" + userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(content().string("Portfolio created successfully"));
    }
}