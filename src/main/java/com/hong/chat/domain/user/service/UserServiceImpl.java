package com.hong.chat.domain.user.service;

import com.hong.chat.domain.user.domain.User;
import com.hong.chat.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    
    // 로그인 실행
    @Override
    public User login(String userId, String password) {
        return userRepository.findByUserIdAndPassword(userId, password)
                .orElseThrow(() -> new IllegalArgumentException("아이디 또는 비밀번호가 잘못되었습니다."));
    }

}
