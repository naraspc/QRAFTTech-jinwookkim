package com.example.QRAFT.portfolio.controller;

import com.example.QRAFT.portfolio.dto.PortfolioRequestDto;
import com.example.QRAFT.portfolio.dto.PortfolioResponseDto;
import com.example.QRAFT.portfolio.service.PortfolioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.sound.sampled.Port;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/portfolio")
public class PortfolioController {

    private final PortfolioService portfolioService;

    /*
    1. 원래대로라면 Path경로로 UUID인 UserId를 받는건 좋지않다고 생각합니다.
    다만, 필수 구현사항에 User관련 내용이 없어 JWT는 추가하지 않았습니다.
    만약 추가해야한다면 JWT를 사용해 유저요청시 받은 토큰에서 userId를 가져오는식으로 구현하고자 합니다.

    2. Restful한 API를 만들기 위해선 엔드포인트에 create 같은 상태를 나타내는 동사를 사용하는게 좋지 않다고 생각 합니다.
    다만 주어진 조건이 Post만 사용하게 되어있어 부득이하게 엔드포인트에 동사를 사용하는점 양해 부탁드립니다.
     */
    @PostMapping("/create/{userId}")
    public ResponseEntity<String> createPortfolio(@RequestBody PortfolioRequestDto portfolioRequestDto, @PathVariable UUID userId) {

        portfolioService.createPortfolioEntityWithStock(portfolioRequestDto, userId);

        return ResponseEntity.ok("Portfolio created successfully");
    }

    @PostMapping("/add/{portfolioId}")
    public ResponseEntity<String> addPersonalStockDataInPortfolio(@RequestBody PortfolioRequestDto.StockEntryDto stockEntryDto, @PathVariable UUID portfolioId) {
        portfolioService.addPersonalStockDataInPortfolio(stockEntryDto, portfolioId);

        return ResponseEntity.ok("Stock added successfully");
    }

    @PostMapping("/delete/{userId}")
    public ResponseEntity<String> deletePortfolio(@RequestBody String portfolioId, @PathVariable UUID userId) {
        portfolioService.deletePortfolio(portfolioId, userId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    @PostMapping("find/{userId}")
    public ResponseEntity<PortfolioResponseDto> findPortfolio(@RequestBody String portfolioID, @PathVariable UUID userId) {

        return ResponseEntity.status(HttpStatus.OK).body(portfolioService.findPortfolio(portfolioID, userId));

    }


}
