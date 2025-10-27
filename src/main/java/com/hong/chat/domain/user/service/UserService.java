package com.hong.chat.domain.user.service;

import com.hong.chat.domain.user.domain.User;

public interface UserService {
    
    // 로그인 실행
    User login(String userId, String password);
}
