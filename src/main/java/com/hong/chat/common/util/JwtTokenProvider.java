package com.hong.chat.common.util;

import com.hong.chat.domain.user.domain.User;
import com.hong.chat.domain.user.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
    private final UserRepository userRepository;
    @Value("${jwt.secret}")
    private String secretKey;
    private final UserDetailsService userDetailsService;
    
    // 액세스 토큰 유효시간
    private final long tokenValidTime = 1000L * 60 * 30; // 30분

    /**
     * JWT 토큰 생성
     * @param user 객체
     * @return JWT 문자열
     */
    public String createToken(User user) {
        // JWT payload 설정
        Claims claims = Jwts.claims().setSubject(user.getUserId());
        claims.put("nickname", user.getNickname());
        claims.put("id", user.getId());

        Date now = new Date();

        // 비밀키를 Key 객체로 변환
        Key key = Keys.hmacShaKeyFor(secretKey.getBytes());
        
        // JWT 빌드 및 서명
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)   // 발급 시각
                .setExpiration(new Date(now.getTime() + tokenValidTime)) // 만료 시각
                .signWith(key, SignatureAlgorithm.HS256)    // 서명(비밀키, 알고리즘)
                .compact(); // 최종 문자열 반환
    }

    /**
     * 토큰으로부터 인증 객체 생성
     * SecurityContextHolder 에서 사용할 수 있는 형태로 변환
     * JWT 안의 정보를 기반으로 Spring Security가 이해할 수 있는 인증 정보로 변환하는 과정
     */
    public Authentication getAuthentication(String token) {
        String userId = getUserId(token);

        // DB 에서 사용자 조회
        User user = userRepository.findByUserId(userId);

        // 사용자 정보로 UserDetails 인증 객체로 변환 생성
        // UserDetails는 인증된 사용자 정보를 담는 표준 객체
        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(user.getUserId())
                .password(user.getPassword())
                .roles("USER")
                .build();

        // UsernamePasswordAuthenticationToken 으로 인증 객체 생성
        // 이 객체는 시큐리티 컨텍스트(SecurityContextHolder)에 저장되어
        // 요청 시 인증된 사용자 정보를 제공
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    /**
     * 토큰에서 userId 추출
     */
    public String getUserId(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    /**
     * 토큰의 유효성 검증
     * 서명 확인
     * 만료 여부 검증
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
