package com.example.QRAFT.stockPriceInfo.controller;

import com.example.QRAFT.stockPriceInfo.dto.StockPriceRequestDto;
import com.example.QRAFT.stockPriceInfo.dto.StockPriceResponseDto;
import com.example.QRAFT.stockPriceInfo.service.StockPriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/stocks")
@RequiredArgsConstructor
public class StockPriceController {


    private final StockPriceService stockPriceService;

    @PostMapping("/findStock")
    public ResponseEntity<List<StockPriceResponseDto>> findStockPriceInfo(@RequestBody StockPriceRequestDto stockPriceRequestDto) {

        return ResponseEntity.ok(stockPriceService.getClosePriceOrDailyReturns(stockPriceRequestDto));

    }
}
