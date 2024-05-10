package com.example.QRAFT.portfolio.entity;

import com.example.QRAFT.common.entity.BaseDateEntity;
import com.example.QRAFT.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.UUID;

@Entity
@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class PortfolioEntity extends BaseDateEntity {
    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;


    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity userEntity;

    @OneToMany(mappedBy = "portfolio", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PortfolioStock> stocks;

    public void updateStocksInPortfolioEntity(List<PortfolioStock> stocks) {
        this.stocks = stocks;
    }
}
