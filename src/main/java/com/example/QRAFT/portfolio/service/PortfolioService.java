package com.example.QRAFT.portfolio.service;


import com.example.QRAFT.portfolio.dto.PortfolioRequestDto;
import com.example.QRAFT.portfolio.entity.PortfolioEntity;
import com.example.QRAFT.portfolio.entity.PortfolioStock;
import com.example.QRAFT.portfolio.repository.PortfolioRepository;
import com.example.QRAFT.user.repository.UserRepository;
import com.example.QRAFT.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;

    // userRepository를 직접 의존하지 않기위해 service에 UserEntity 관련 메소드 정의 후 호출
    private final UserService userService;


    @Transactional
    public void createPortfolio(PortfolioRequestDto requestDto, UUID userId) {

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

}
