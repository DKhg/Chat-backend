package com.hong.chat.domain.user.dto;

import com.hong.chat.domain.user.domain.User;
import lombok.*;

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
    
    // Entity -> Dto 변환
    public static UserDto fromEntity(User user) {
        return UserDto.builder()
                .id(user.getId())
                .userId(user.getUserId())
                .password(user.getPassword())
                .nickname(user.getNickname())
                .build();
    }

}
