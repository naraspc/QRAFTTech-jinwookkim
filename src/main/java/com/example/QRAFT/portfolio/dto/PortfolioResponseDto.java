package com.example.QRAFT.portfolio.dto;

import com.example.QRAFT.portfolio.entity.PortfolioEntity;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;


public record PortfolioResponseDto(
        String portfolioName,
        Map<LocalDate,BigDecimal> totalValues,
        LocalDate createDate
){
    //Entity의 변환을 위한 팩토리 메소드
    public static PortfolioResponseDto fromEntity(PortfolioEntity entity, Map<LocalDate, BigDecimal> totalValues) {
        return new PortfolioResponseDto(
                entity.getName(),
                totalValues,
                entity.getCreatedAt()
        );
    }
}
