package com.hong.chat.common.filter;

import com.hong.chat.common.util.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * 모든 요청마다 실행되는 Security Filter
 * Authorization 헤더에서 JWT 토큰을 꺼내 검증하고,
 * 유효한 경우 Spring Security의 인증 컨텍스트에 사용자 정보를 저장함
 * 이렇게 하면 이후 Controller 에서 인증된 사용자 정보를 꺼내 쓸 수 있음
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    /**
     * JwtTokenProvider는 토큰 생성, 검증, 사용자 정보 추출을 담당하는 유틸 클래스
     */
    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();

        //  회원가입/로그인 요청은 JWT 검사 제외 ( 403 에러가 뜸 )
        if (path.startsWith("/api/user/login") || path.startsWith("/api/user/join")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Authorization 헤더에서 JWT 토큰 추출
        String token = resolveToken(request);

        if (token != null && jwtTokenProvider.validateToken(token)) {
            // Authentication(인증정보) 객체 생성
            Authentication authentication = jwtTokenProvider.getAuthentication(token);

            // SecurityContextHolder에 인증 객체 등록 → 이후 Security가 인증된 사용자로 인식
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // 다음 필터로 요청 전달
        filterChain.doFilter(request, response);
    }

    /**
     * HTTP 요청의 Authorization 헤더에서 JWT 토큰을 추출하는 메서드
     * @param request 클라이언트 요청
     * @return JWT 토큰 문자열
     */
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        // 문자열이 존재하고, "Bearer "로 시작하면 실제 토큰 부분만 추출
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        // 헤더가 없거나 잘못된 형식일 경우
        return null;
    }
}
