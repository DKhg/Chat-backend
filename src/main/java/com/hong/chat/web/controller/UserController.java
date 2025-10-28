package com.hong.chat.web.controller;

import com.hong.chat.domain.user.domain.User;
import com.hong.chat.domain.user.dto.UserDto;
import com.hong.chat.domain.user.repository.UserRepository;
import com.hong.chat.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    // final 은 Lombok이  @RequiredArgsConstructor 생성자 자동생성 시 주입 대상으로 인식하기 위한 표시
    private final UserService userService;
    private final UserRepository userRepository;

    // 로그인 실행
    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody UserDto requestUserDto) {
        // 로그인
        User user = userService.login(requestUserDto.getUserId(), requestUserDto.getPassword());
        UserDto responseUserDto = UserDto.fromEntity(user);
        
        responseUserDto.setPassword(null);
        return ResponseEntity.ok(responseUserDto);
    }

    // 사용자 목록 조회
    @GetMapping("/users")
    public List<UserDto> getUserList() {
        return userService.getUserList();
    }
}
