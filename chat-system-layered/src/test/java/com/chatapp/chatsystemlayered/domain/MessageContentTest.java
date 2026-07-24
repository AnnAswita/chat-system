package com.chatapp.chatsystemlayered.domain;

import com.chatapp.chatsystemlayered.domain.chat.MessageContent;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MessageContentTest {

    @Test
    void testValidContent() {
        MessageContent content = new MessageContent("Hello");
        assertEquals("Hello", content.getText());
    }

    @Test
    void testInvalidContent() {
        assertThrows(IllegalArgumentException.class, () -> new MessageContent(""));
        assertThrows(IllegalArgumentException.class, () -> new MessageContent("   "));
        assertThrows(IllegalArgumentException.class, () -> new MessageContent(null));
    }
}
