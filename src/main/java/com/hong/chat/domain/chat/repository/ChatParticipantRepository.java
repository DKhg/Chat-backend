package com.hong.chat.domain.chat.repository;

import com.hong.chat.domain.chat.domain.ChatParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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

    /**
     * 특정 참여자의 채팅방 나가기
     * 단순 조회는 @Query만 써도됨 / DELETE, UPDATE, INSERT @Modifying  붙여주기
     */
    @Modifying
    @Transactional
    @Query("""
    DELETE FROM ChatParticipant c
     WHERE 1=1
       AND c.chatRoom.id = :roomId
       AND c.user.userId = :userId
    """)
    void deleteByChatRoomIdAndUserId(@Param("roomId") Long roomId, @Param("userId") String userId);

    double countByChatRoom_id(long chatRoomId);
}
