package com.hong.chat.domain.user.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String userId;              // 로그인 ID

    @Column(nullable = false)
    private String password;            // 암호화된 비밀번호

    @Column
    private String nickname;            // 닉네임

    @Column
    private String email;               // 이메일

    @Column
    private String username;            // 이름

    @Column
    private LocalDateTime createAt;     // 생성일자

}
