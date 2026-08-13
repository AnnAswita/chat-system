package com.chatapp.chatsystemeda.events;

import java.time.Instant;

public class MessageSentEvent {

    private String messageId;
    private String userId;
    private String roomId;
    private String content;
    private Instant timestamp;
    public MessageSentEvent(){

    }
    public MessageSentEvent(String messageId, String userId, String roomId, String content, Instant timestamp) {
        this.messageId = messageId;
        this.userId = userId;
        this.roomId = roomId;
        this.content = content;
        this.timestamp = timestamp;
    }

    public String getMessageId() { return messageId; }
    public String getUserId() { return userId; }
    public String getRoomId() { return roomId; }
    public String getContent() { return content; }
    public Instant getTimestamp() { return timestamp; }
}
