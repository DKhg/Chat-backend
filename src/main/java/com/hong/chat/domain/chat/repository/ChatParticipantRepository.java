package com.hong.chat.domain.chat.repository;

import com.hong.chat.domain.chat.domain.ChatParticipant;
import com.hong.chat.domain.chat.domain.ChatRoom;
import com.hong.chat.domain.user.domain.User;
import org.springframework.cglib.core.Local;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ChatParticipantRepository extends JpaRepository<ChatParticipant,Long> {

    // 특정 채팅방의 참여자 목록
    List<ChatParticipant> findByChatRoom_Id(Long roomId);

    // 특정 참여자 조회
    Optional<ChatParticipant> findByChatRoom_IdAndUser_UserId(Long roomId, String userId);

    // 특정 사용자의 채팅방 목록
    List<ChatParticipant> findByUser_userId(String userId);
    
    // 메시지 읽지 않은 수 조회
    @Query("""
    SELECT COUNT(c)
      FROM ChatParticipant c
     WHERE c.chatRoom.id = :roomId
       AND (c.lastReadAt IS NULL OR c.lastReadAt < :messageCreatedAt)
    """)
    int countUnreadUsers(@Param("roomId") Long roomId, @Param("messageCreatedAt") LocalDateTime messageCreatedAt);
}
