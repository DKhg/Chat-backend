package com.hong.chat.web.controller;

import com.hong.chat.domain.user.domain.User;
import com.hong.chat.domain.user.dto.UserDto;
import com.hong.chat.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    // final 은 Lombok이  @RequiredArgsConstructor 생성자 자동생성 시 주입 대상으로 인식하기 위한 표시
    private final UserService userService;
    
    // 로그인 실행
    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody UserDto requestUserDto) {
        // 로그인
        User user = userService.login(requestUserDto.getUserId(), requestUserDto.getPassword());
        UserDto responseUserDto = UserDto.fromEntity(user);
        
        responseUserDto.setPassword(null);
        return ResponseEntity.ok(responseUserDto);
    }
}
