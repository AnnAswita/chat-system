package com.chatapp.chatsystemlayered.domain;


import com.chatapp.chatsystemlayered.domain.chat.ChatRoom;
import com.chatapp.chatsystemlayered.domain.chat.Message;
import com.chatapp.chatsystemlayered.domain.chat.MessageContent;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class ChatRoomTest {

    @Test
    void testAddParticipant() {
        ChatRoom room = new ChatRoom("room1");
        room.addParticipant("user1");

        assertTrue(room.getMessages().isEmpty());
    }

    @Test
    void testAddMessage() {
        ChatRoom room = new ChatRoom("room1");
        MessageContent content = new MessageContent("Hello");
        Message message = new Message(1, "user1", content, Instant.now());

        room.addMessage(message);

        assertEquals(1, room.getMessages().size());
        assertEquals("Hello", room.getMessages().get(0).getContent().getText());
    }

    @Test
    void testMessageSequence() {
        ChatRoom room = new ChatRoom("room1");

        assertEquals(1, room.nextMessageId());
        assertEquals(2, room.nextMessageId());
    }
}