package com.hong.chat.domain.user.repository;

import com.hong.chat.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    
    // 로그인 실행
    Optional<User> findByUserIdAndPassword(String userId, String password);
    
    // 사용자 조회
    User findByUserId(String userId);
}