package com.hong.chat.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
// STOMP 프로토콜 기반 WebSocket 메시지 브로커 사용을 활성화
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws-chat")     // 클라이언트가 연결할 엔드포인트 설정
                .setAllowedOriginPatterns("*")      // Vue3에서 Cors 방지
                .withSockJS();                      // WebSocket을 지원하지 않는 브라우저에서도 연결이 가능하도록
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        /* 클라이언트가 구독할 prefix
         * 구독 : 클라이언트가 서버에게 “이 채널 메시지 보내줘” 요청
         * 보내는 건 app, 받는 건 topic (표준화)
         */
        registry.enableSimpleBroker("/topic");

        /* 서버로 메시지 보낼 때 사용하는 prefix
         * 서버로 “보내는 메시지”의 입구
         * 서버 쪽의 핸들러 메서드(@MessageMapping) 와 매칭
         */
        registry.setApplicationDestinationPrefixes("/app");
    }
}
