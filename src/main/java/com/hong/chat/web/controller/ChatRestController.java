package com.hong.chat.web.controller;

import com.hong.chat.domain.chat.domain.ChatParticipant;
import com.hong.chat.domain.chat.domain.ChatRoom;
import com.hong.chat.domain.chat.dto.ChatMessageDto;
import com.hong.chat.domain.chat.dto.ChatRoomDto;
import com.hong.chat.domain.chat.service.ChatService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        return list.stream().map(ChatParticipant::getChatRoom).filter(c -> "N".equals(c.getDeleteYn())).collect(Collectors.toList());
    }
    
    // 채팅방 메시지 조회
    @GetMapping("/{roomId}/messages")
    public List<ChatMessageDto> getMessage(@PathVariable Long roomId, @RequestParam boolean includeUnreadCount) {
        return chatService.getMessage(roomId, includeUnreadCount);
    }

    // 채팅방 생성
    @PostMapping("/createChatRoom")
    public ResponseEntity<ChatRoom> createChatRoom(@ModelAttribute ChatRoomDto chatRoomDto) {
        return  ResponseEntity.ok(chatService.createChatRoom(chatRoomDto));
    }

    // 채팅방 나가기
    @PostMapping("/leaveChatRoom")
    public ResponseEntity<?> leaveChatRoom(@RequestParam Long roomId, @RequestParam String userId) {
        try {
            chatService.leaveChatRoom(roomId, userId);
            return  ResponseEntity.ok().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("채팅방 또는 사용자 정보를 찾을 수 없습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("채팅방 나가기 중 오류가 발생했습니다.");
        }
    }

}
