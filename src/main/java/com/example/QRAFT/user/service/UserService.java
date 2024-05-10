package com.example.QRAFT.user.service;

import com.example.QRAFT.user.entity.UserEntity;
import com.example.QRAFT.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserEntity findUserEntity(UUID userId) {
        return userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다."));
    }
}
