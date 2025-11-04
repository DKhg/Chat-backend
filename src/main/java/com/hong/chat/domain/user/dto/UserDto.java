package com.hong.chat.domain.user.dto;

import com.hong.chat.domain.user.domain.User;
import jakarta.persistence.Column;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {

    private Long id;
    private String userId;
    private String password;
    private String nickname;
    private String email;
    private String username;


    // Entity -> Dto 변환
    public static UserDto fromEntity(User user) {
        return UserDto.builder()
                .id(user.getId())
                .userId(user.getUserId())
                .password(user.getPassword())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .username(user.getUsername())
                .build();
    }

    // Dto -> Entity
    public User toEntity(PasswordEncoder encoder) {
        return User.builder()
                .userId(userId)
                .email(email)
                .password(encoder.encode(password))
                .nickname(nickname)
                .username(username)
                .createAt(LocalDateTime.now())
                .build();
    }

}
