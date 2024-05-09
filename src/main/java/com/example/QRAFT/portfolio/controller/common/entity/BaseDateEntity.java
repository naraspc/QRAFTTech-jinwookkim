package com.example.QRAFT.portfolio.controller.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@MappedSuperclass
@Getter
@NoArgsConstructor
@SuperBuilder
public abstract class BaseDateEntity {

    @Column(nullable = false, updatable = false)
    private LocalDate createdAt;

    @Column(nullable = false)
    private LocalDate updatedAt;


    /*
    nullable = false 로 설계 했기 때문에
    생성시 updateAt도 생성 일자로 설정함
    */

    @PrePersist
    protected void onCreate() {
        LocalDate now = LocalDate.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDate.now();
    }


}
