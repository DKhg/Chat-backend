package com.hong.chat.web.controller;

import com.hong.chat.domain.chat.dto.ChatMessageDto;
import com.hong.chat.domain.chat.dto.MessageReadDto;
import com.hong.chat.domain.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    // 서버에서 특정 topic 구독자들에게 메시지를 전송하는 Spring 내장 인터페이스
    private final SimpMessageSendingOperations messagingTemplate;

    /**
     * Payload → WebSocket 에서 수신된 JSON 메시지를 DTO 객체로 변환해줌
     * 클라이언트가 보낸 채팅 메시지를 받아서 → 해당 채팅방 구독자에게 방송
     * @param chatMessage
     */
    @MessageMapping("/chatSend")
    public void sendMessage(@Payload ChatMessageDto chatMessage) {
    
        // 메시지 보낸 시간 설정
        chatMessage.setCreatedAt(LocalDateTime.now());
        
        // 메시지 타입에 메시지 설정
        switch(chatMessage.getMessageType()) {
            case ENTER : chatMessage.setContent(chatMessage.getSenderId() + "님이 입장했습니다.");
                break;
            case LEAVE : chatMessage.setContent(chatMessage.getSenderId() + "님이 퇴장했습니다.");
                break;
            default:
                break;
        }
        
        // 메시지 DB 저장
        chatService.saveMessage(chatMessage);

        // 실행중인 브로커의 메모리 안에서만 존재하는 임시 라우팅 경로
        messagingTemplate.convertAndSend("/topic/chatRoom/" + chatMessage.getRoomId(), chatMessage);
    }

    /**
     * 사용자가 메시지를 읽었을 때 → 읽음 정보를 브로드캐스트
     * Payload → WebSocket 에서 수신된 JSON 메시지를 DTO 객체로 변환해줌
     * @param messageRead
     */
    @MessageMapping("/chatRead")
    public void readMessage(@Payload MessageReadDto messageRead) {
        messageRead.setRead(true);
        messageRead.setReadAt(LocalDateTime.now());
        messagingTemplate.convertAndSend("/topic/chatRoom/" + messageRead.getRoomId() + "/read", messageRead);
    }
}
