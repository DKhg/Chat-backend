package com.hong.chat.domain.chat.repository;

import com.hong.chat.domain.chat.domain.ChatMessage;
import com.hong.chat.domain.chat.domain.ChatRoom;
import com.hong.chat.domain.chat.dto.ChatMessageDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage,Long> {
    
    // 채팅방의 메시지를 시간순으로 조회
    List<ChatMessage> findByChatRoom_IdOrderByCreatedAtAsc(Long roomId);
}
