package com.chatapp.chatsystemeda.infrastructure.persistence;

import com.chatapp.chatsystemeda.domain.chat.ChatRoom;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class RoomRepository {

    private final Map<String, ChatRoom> rooms = new ConcurrentHashMap<>();

    public ChatRoom findOrCreate(String roomId) {
        return rooms.computeIfAbsent(roomId, ChatRoom::new);
    }
    public void save(ChatRoom room) {
        rooms.put(room.getRoomId(), room);
    }
}
