package com.chatapp.chatsystemlayered.infrastructure;

import com.chatapp.chatsystemlayered.domain.chat.ChatRoom;
import com.chatapp.chatsystemlayered.infrastructure.persistence.InMemoryChatRepository;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryChatRepositoryTest {

    @Test
    void testSaveAndFind() {
        InMemoryChatRepository repo = new InMemoryChatRepository();
        ChatRoom room = new ChatRoom("room1");

        repo.save(room);

        Optional<ChatRoom> found = repo.findById("room1");

        assertTrue(found.isPresent());
        assertEquals("room1", found.get().getId());
    }

    @Test
    void testFindMissingRoom() {
        InMemoryChatRepository repo = new InMemoryChatRepository();
        assertTrue(repo.findById("missing").isEmpty());
    }
}
