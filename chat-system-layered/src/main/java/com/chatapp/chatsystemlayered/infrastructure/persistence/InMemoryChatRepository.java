package com.chatapp.chatsystemlayered.infrastructure.persistence;

import com.chatapp.chatsystemlayered.domain.chat.ChatRoom;
import com.chatapp.chatsystemlayered.domain.repository.ChatRepository;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryChatRepository implements ChatRepository {

    private final Map<String, ChatRoom> store = new ConcurrentHashMap<>();

    @Override
    public Optional<ChatRoom> findById(String id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public void save(ChatRoom room) {
        store.put(room.getId(), room);
    }
}