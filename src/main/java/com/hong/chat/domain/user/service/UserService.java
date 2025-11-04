package com.hong.chat.domain.user.service;

import com.hong.chat.domain.user.domain.User;
import com.hong.chat.domain.user.dto.UserDto;

import java.util.List;

public interface UserService {
    
    // 로그인 실행
    User login(String userId, String password);

    // 사용자 목록 조회
    List<UserDto> getUserList(String userId);
    
    // 회원가입
    User join(UserDto userDto);
}
