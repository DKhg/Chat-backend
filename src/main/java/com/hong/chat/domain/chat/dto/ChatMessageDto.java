package com.hong.chat.domain.chat.dto;

import com.hong.chat.domain.chat.domain.ChatMessage;
import com.hong.chat.domain.chat.domain.ChatRoom;
import com.hong.chat.domain.chat.domain.MessageType;
import com.hong.chat.domain.user.domain.User;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessageDto {

    private MessageType messageType;    // ENTER, TALK, LEAVE
    private Long roomId;                // 채팅방 ID (UUID 등)
    private String senderId;            // 보낸 사람 ID
    private String senderName;          // 보낸 사람 이름
    private String content;             // 메시지 내용
    private LocalDateTime createdAt;    // 보낸 시각
    private Long unreadCount;           // 메시지 읽지 않은 사람 수


    /**
     * 저장용 : DTO → Entity
     */
    public ChatMessage toEntity(ChatRoom room, User user) {
        return ChatMessage.builder()
                .chatRoom(room)
                .sender(user)
                .messageType(this.messageType)
                .content(this.content)
                .createdAt(LocalDateTime.now())
                .build();
    }

    /**
     * 단순 조회용 : Entity → DTO
     */
    public static ChatMessageDto fromEntity(ChatMessage message) {
        return ChatMessageDto.builder()
                .roomId(message.getChatRoom().getId())
                .senderId(message.getSender() != null ? message.getSender().getUserId() : null)
                .senderName(message.getSender() != null ? message.getSender().getNickname() : "알 수 없음")
                .messageType(message.getMessageType())
                .content(message.getContent())
                .createdAt(message.getCreatedAt())
                .build();
    }

    /**ㄹ
     * 읽음 카운트 포함 : Entity → DTO
     */
    public static ChatMessageDto fromEntityWithUnreadCount(ChatMessage message, long unreadCount) {
        return ChatMessageDto.builder()
                .roomId(message.getChatRoom().getId())
                .senderId(message.getSender() != null ? message.getSender().getUserId() : null)
                .senderName(message.getSender() != null ? message.getSender().getNickname() : "알 수 없음")
                .messageType(message.getMessageType())
                .content(message.getContent())
                .createdAt(message.getCreatedAt())
                .unreadCount(unreadCount)
                .build();
    }
}
