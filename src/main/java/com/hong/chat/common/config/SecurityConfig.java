package com.hong.chat.common.config;

import com.hong.chat.common.filter.JwtAuthenticationFilter;
import com.hong.chat.common.util.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * CORS 허용 설정 (Vue 프론트엔드와 연동)
 * CSRF 비활성화 (REST API 서버이므로)
 * 로그인 관련 URL은 인증 없이 접근 허용
 * 나머지 요청은 인증 필요
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * @param http HttpSecurity 객체 ( 스프링 시큐리티 HTTP 보안 설정 )
     * @param corsConfigurationSource CORS 설정 Bean
     * @return SecurityFilterChain ( 스프링 시큐리티에서 사용할 필터 체인 )
     */
    @Bean
    public SecurityFilterChain configure(HttpSecurity http, CorsConfigurationSource corsConfigurationSource, JwtTokenProvider jwtTokenProvider) throws Exception {
        http
                // CORS(Cross-Origin Resource Sharing) 허용
                // Vue -> Spring-Boot 간 요청 허용 설정
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                // CSRF 보호 비활성화
                // REST API 서버는 세션을 사용하지 않기 때문에 일반적으로 비활성화
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth.requestMatchers("/api/**","/ws-chat/**").permitAll()
                        .anyRequest().authenticated())
                // UsernamePasswordAuthenticationFilter 실행되기전에 만든 JwtAuthenticationFilter 먼저 실행되게
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * CORS 설정 Bean
     * Vue 개발 서버에서 오는 요청 허용
     * 특정 HTTP 메서드(GET, POST 등) 허용
     * 모든 헤더 허용
     * 자격 증명(쿠키, Authorization 헤더) 허용
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:3000"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
