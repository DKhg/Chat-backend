package com.hong.chat.web.controller;

import com.hong.chat.common.util.JwtTokenProvider;
import com.hong.chat.domain.user.domain.User;
import com.hong.chat.domain.user.dto.UserDto;
import com.hong.chat.domain.user.repository.UserRepository;
import com.hong.chat.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    // final 은 Lombok이  @RequiredArgsConstructor 생성자 자동생성 시 주입 대상으로 인식하기 위한 표시
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    // 로그인 실행
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody UserDto requestUserDto) {
        // 로그인
        User user = userService.login(requestUserDto.getUserId(), requestUserDto.getPassword());
        UserDto responseUserDto = UserDto.fromEntity(user);
        responseUserDto.setPassword(null);

        // JWT 토큰 생성
        String accessToken = jwtTokenProvider.createToken(user);

        Map<String, Object> res = new HashMap<>();
        res.put("accessToken", accessToken);
        res.put("user", responseUserDto);

        return ResponseEntity.ok(res);
    }

    // 사용자 목록 조회 ( 자기 자신을 제외 )
    @GetMapping("/users")
    public List<UserDto> getUserList(@RequestParam String userId) {
        return userService.getUserList(userId);
    }
}
