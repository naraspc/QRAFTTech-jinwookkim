package com.example.QRAFT.stockPriceInfo.controller;

import com.example.QRAFT.stockPriceInfo.dto.StockPriceRequestDto;
import com.example.QRAFT.stockPriceInfo.dto.StockPriceResponseDto;
import com.example.QRAFT.stockPriceInfo.service.StockPriceService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = StockPriceController.class)
class StockPriceControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StockPriceService stockPriceService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("주식 조회 테스트")
    public void findStockPriceInfo_Success() throws Exception {
        // Given
        String stockCode = "AAPL";
        LocalDate startDate = LocalDate.of(2021, 1, 1);
        LocalDate endDate = LocalDate.of(2021, 1, 10);
        StockPriceRequestDto requestDto = new StockPriceRequestDto(stockCode, startDate, endDate, "Price");

        List<StockPriceResponseDto> expectedResponse = List.of(
                new StockPriceResponseDto(startDate, new BigDecimal("150"), "Price")
        );

        when(stockPriceService.getClosePriceOrDailyReturns(any(StockPriceRequestDto.class)))
                .thenReturn(expectedResponse);

        // Act & Assert
        mockMvc.perform(post("/api/stocks/findStock")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].date").value(startDate.toString()))
                .andExpect(jsonPath("$[0].value").value(150))
                .andExpect(jsonPath("$[0].type").value("Price"));
    }

}