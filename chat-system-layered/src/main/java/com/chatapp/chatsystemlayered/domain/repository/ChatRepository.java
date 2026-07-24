package com.chatapp.chatsystemlayered.domain.repository;

import com.chatapp.chatsystemlayered.domain.chat.ChatRoom;

import java.util.Optional;

public interface ChatRepository {
    Optional<ChatRoom> findById(String id);
    void save(ChatRoom room);
}