package com.ann.chat.chatservice.infrastructure;

import com.ann.chat.chatservice.domain.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByRoom(String room);
}
