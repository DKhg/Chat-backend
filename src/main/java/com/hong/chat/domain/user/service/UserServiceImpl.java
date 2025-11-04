package com.hong.chat.domain.user.service;

import com.hong.chat.domain.user.domain.User;
import com.hong.chat.domain.user.dto.UserDto;
import com.hong.chat.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 로그인 실행
    @Override
    public User login(String userId, String password) {
        // 사용자 조회
        User user = userRepository.findByUserId(userId);
        
        // 비밀번호 비교
        if(!passwordEncoder.matches(password, user.getPassword())){
            throw new IllegalArgumentException("아이디 또는 비밀번호가 일치하지 않습니다.");
        }

        return user;
    }
    
    // 자기자신을 제외한 사용자 조회
    @Override
    public List<UserDto> getUserList(String userId) {

        List<User> userList = userRepository.findAllExceptUserId(userId);

        return userList.stream()
                .map(user -> UserDto.builder()
                        .id(user.getId())
                        .userId(user.getUserId())
                        .nickname(user.getNickname())
                        .build()
                ).toList();
    }

    // 회원가입
    @Override
    public User join(UserDto userDto) {
        User user = userDto.toEntity(passwordEncoder);
        return userRepository.save(user);
    }


}
