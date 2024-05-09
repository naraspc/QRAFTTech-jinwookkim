package com.example.QRAFT.user.entity;

import com.example.QRAFT.portfolio.controller.common.entity.BaseDateEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user")
public class UserEntity extends BaseDateEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, length = 26)
    private String userName;

    @Column(nullable = false)
    private LocalDate registerDate;


}
