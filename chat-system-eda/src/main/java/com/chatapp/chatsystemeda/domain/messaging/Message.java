package com.chatapp.chatsystemeda.domain.messaging;

import java.time.Instant;
import java.util.UUID;

public class Message {

    private final String id;
    private final String userId;
    private final String roomId;
    private final String content;
    private final Instant timestamp;

    public Message(String userId, String roomId, String content) {
        this.id = UUID.randomUUID().toString();
        this.userId = userId;
        this.roomId = roomId;
        this.content = content;
        this.timestamp = Instant.now();
    }

    public String getId() { return id; }
    public String getUserId() { return userId; }
    public String getRoomId() { return roomId; }
    public String getContent() { return content; }
    public Instant getTimestamp() { return timestamp; }
}
