package com.hong.chat.domain.user.repository;

import com.hong.chat.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    
    // 로그인 실행
    Optional<User> findByUserIdAndPassword(String userId, String password);
    
    // 사용자 조회
    User findByUserId(String userId);

    // 자기 자신을 제외한 사용자 목록 조회
    @Query("SELECT u " +
            " FROM User u " +
            "WHERE u.userId <> :userId")
    List<User> findAllExceptUserId(@Param("userId") String userId);

}