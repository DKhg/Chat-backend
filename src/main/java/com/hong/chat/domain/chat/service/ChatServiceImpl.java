package com.hong.chat.domain.chat.service;

import com.hong.chat.domain.chat.domain.ChatMessage;
import com.hong.chat.domain.chat.domain.ChatParticipant;
import com.hong.chat.domain.chat.domain.ChatRoom;
import com.hong.chat.domain.chat.dto.ChatMessageDto;
import com.hong.chat.domain.chat.repository.ChatMessageRepository;
import com.hong.chat.domain.chat.repository.ChatParticipantRepository;
import com.hong.chat.domain.chat.repository.ChatRoomRepository;
import com.hong.chat.domain.user.domain.User;
import com.hong.chat.domain.user.dto.UserDto;
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
    public ChatMessage saveMessage(ChatMessageDto dto) {

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
        ChatMessage savedMessage = chatMessageRepository.save(message);

        // 보낸 사람의 마지막 읽은 시간 갱신
        chatParticipantRepository.findByChatRoom_IdAndUser_UserId(room.getId(), user.getUserId())
                .ifPresent(p -> {
                    p.setLastReadAt(savedMessage.getCreatedAt());
                    chatParticipantRepository.save(p);
                });


        return savedMessage;
    }
    
    // 메시지 조회
    @Override
    public List<ChatMessageDto> getMessage(Long roomId, boolean includeUnreadCount) {

        // 생성일자 오름차순으로 채팅방 메시지 조회
        List<ChatMessage> messages = chatMessageRepository.findByChatRoom_IdOrderByCreatedAtAsc(roomId);

        return messages.stream()
                .map(msg -> {
                    // 읽지않은 수 포함 조회
                    if (includeUnreadCount) {
                        long unreadCount = chatParticipantRepository.countUnreadUsers(roomId, msg.getCreatedAt());
                        return ChatMessageDto.fromEntityWithUnreadCount(msg, unreadCount);
                    } else {
                        return ChatMessageDto.fromEntity(msg);
                    }
                })
                .toList();
    }
    
    // 사용자 채팅방 목록 조회
    @Override
    public List<ChatParticipant> getUserRooms(String userId) {
        return chatParticipantRepository.findByUser_userId(userId);
    }

}
