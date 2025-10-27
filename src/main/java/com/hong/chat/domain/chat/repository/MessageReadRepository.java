package com.hong.chat.domain.chat.repository;

import com.hong.chat.domain.chat.domain.ChatMessage;
import com.hong.chat.domain.chat.domain.MessageRead;
import com.hong.chat.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MessageReadRepository extends JpaRepository<MessageRead,Long> {

    // 특정 유저가 특정 메시지를 읽었는지 확인
    Optional<MessageRead> findByChatMessageAndUser(ChatMessage message, User user);

    // 메시지 기준으로 읽은 사람 수 구하기
    Long countByChatMessageAndIsReadTrue(ChatMessage chatMessage);
}
