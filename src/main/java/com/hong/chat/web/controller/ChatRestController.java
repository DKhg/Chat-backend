package com.hong.chat.web.controller;

import com.hong.chat.domain.chat.domain.ChatParticipant;
import com.hong.chat.domain.chat.domain.ChatRoom;
import com.hong.chat.domain.chat.dto.ChatMessageDto;
import com.hong.chat.domain.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatRestController {

    private final ChatService chatService;
    
    // 채팅방 목록 조회
    @GetMapping("/getChatRooms")
    public List<ChatRoom> getChatRooms(@RequestParam String userId) {
        List<ChatParticipant> list = chatService.getUserRooms(userId);
        return list.stream().map(ChatParticipant::getChatRoom).collect(Collectors.toList());
    }
    
    // 채팅방 메시지 조회
    @GetMapping("/{roomId}/messages")
    public List<ChatMessageDto> getMessage(@PathVariable Long roomId) {
        return chatService.getMessage(roomId).stream().map(ChatMessageDto::fromEntity).toList();
    }

}
