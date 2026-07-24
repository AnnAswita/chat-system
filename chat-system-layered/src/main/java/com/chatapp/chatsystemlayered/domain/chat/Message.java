package com.chatapp.chatsystemlayered.domain.chat;

import java.time.Instant;

public class Message {
    private final long id;
    private final String senderId;
    private final MessageContent content;
    private final Instant timestamp;

    public Message(long id, String senderId, MessageContent content, Instant timestamp) {
        this.id = id;
        this.senderId = senderId;
        this.content = content;
        this.timestamp = timestamp;
    }

    public long getId() { return id; }
    public String getSenderId() { return senderId; }
    public MessageContent getContent() { return content; }
    public Instant getTimestamp() { return timestamp; }
}