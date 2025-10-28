package com.hong.chat.domain.chat.service;

import com.hong.chat.domain.chat.domain.ChatMessage;
import com.hong.chat.domain.chat.domain.ChatParticipant;
import com.hong.chat.domain.chat.dto.ChatMessageDto;

import java.util.List;

public interface ChatService {

    // 메시지 저장
    ChatMessage saveMessage(ChatMessageDto dto);

    // 채팅방별 메시지 조회
    List<ChatMessageDto> getMessage(Long roomId, boolean includeUnreadCount);

    // 참가자가 속한 채팅방 목록 조회
    List<ChatParticipant> getUserRooms(String userId);
}
