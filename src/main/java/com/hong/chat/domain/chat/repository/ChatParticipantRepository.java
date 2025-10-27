package com.hong.chat.domain.chat.repository;

import com.hong.chat.domain.chat.domain.ChatParticipant;
import com.hong.chat.domain.chat.domain.ChatRoom;
import com.hong.chat.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatParticipantRepository extends JpaRepository<ChatParticipant,Long> {

    // 특정 채팅방에 속한 사용자 목록
    List<ChatParticipant> findByChatRoom(ChatRoom chatRoom);

    // 특정 사용자가 채팅방에 참여 중인지 확인
    Optional<ChatParticipant> findByChatRoomAndUser(ChatRoom chatRoom, User user);

    List<ChatParticipant> findByUser_userId(String userId);
}
