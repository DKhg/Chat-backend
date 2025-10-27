package com.hong.chat.domain.chat.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageReadDto {
    private Long messageId;
    private String roomId;
    private String userId;
    private boolean isRead;
    private LocalDateTime readAt;
}
