package com.hong.chat.domain.chat.service;

import com.hong.chat.domain.chat.domain.ChatMessage;
import com.hong.chat.domain.chat.domain.ChatParticipant;
import com.hong.chat.domain.chat.domain.ChatRoom;
import com.hong.chat.domain.chat.dto.ChatMessageDto;
import com.hong.chat.domain.chat.repository.ChatMessageRepository;
import com.hong.chat.domain.chat.repository.ChatParticipantRepository;
import com.hong.chat.domain.chat.repository.ChatRoomRepository;
import com.hong.chat.domain.user.domain.User;
import com.hong.chat.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatServiceImpl implements ChatService{

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatParticipantRepository chatParticipantRepository;
    private final UserRepository userRepository;

    // 메시지 저장
    @Override
    public void saveMessage(ChatMessageDto dto) {

        // 채팅방 조회
        ChatRoom room = chatRoomRepository.findById(dto.getRoomId())
                .orElseThrow(() -> new IllegalArgumentException("채팅방을 찾을 수 없습니다."));
        
        // 참가자 조회
        User user = userRepository.findByUserId(dto.getSenderId());

        // 메시지 생성 및 저장
        ChatMessage message = ChatMessage.builder()
                .chatRoom(room)
                .content(dto.getContent())
                .messageType(dto.getMessageType())
                .createdAt(LocalDateTime.now())
                .sender(user)
                .build();

        chatMessageRepository.save(message);
    }
    
    // 메시지 조회
    @Override
    public List<ChatMessage> getMessage(Long roomId) {
        
        // 생성일자 오름차순으로 채팅방 메시지 조회
        return chatMessageRepository.findByChatRoom_IdOrderByCreatedAtAsc(roomId);
    }

    @Override
    public List<ChatParticipant> getUserRooms(String userId) {
        return chatParticipantRepository.findByUser_userId(userId);
    }

}
