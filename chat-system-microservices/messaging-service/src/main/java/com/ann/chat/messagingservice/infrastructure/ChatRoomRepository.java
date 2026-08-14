package com.ann.chat.messagingservice.infrastructure;

import com.ann.chat.messagingservice.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
}
