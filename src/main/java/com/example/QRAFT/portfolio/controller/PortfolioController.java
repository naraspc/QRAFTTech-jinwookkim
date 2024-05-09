package com.example.QRAFT.portfolio.controller;

import com.example.QRAFT.portfolio.dto.PortfolioRequestDto;
import com.example.QRAFT.portfolio.service.PortfolioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/portfolio")
public class PortfolioController {

    private final PortfolioService portfolioService;

    /*
    원래대로라면 Path경로로 UUID인 UserId를 받는건 좋지않다고 생각합니다.
    다만, 필수 구현사항에 User관련 내용이 없어 JWT는 추가하지 않았습니다.
    만약 추가해야한다면 JWT를 사용해 유저요청시 받은 토큰에서 userId를 가져오는식으로 구현하고자 합니다.
     */
    @PostMapping("/create/{userId}")
    public ResponseEntity<String> createPortfolio(@RequestBody PortfolioRequestDto portfolioRequestDto, @PathVariable UUID userId) {

        portfolioService.createPortfolio(portfolioRequestDto, userId);

        return ResponseEntity.ok("Portfolio created successfully");
    }
}
