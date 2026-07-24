package com.chatapp.chatsystemlayered.domain.chat;

public class MessageContent {
    private final String text;

    public MessageContent(String text) {
        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException("Message content cannot be empty");
        }
        this.text = text;
    }

    public String getText() { return text; }
}