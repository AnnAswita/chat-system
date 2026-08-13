package com.chatapp.chatsystemeda.events;

import java.time.Instant;

public class PresenceUpdatedEvent {

    private String userId;
    private String status;
    private Instant timestamp;

    public PresenceUpdatedEvent(){

    }
    public PresenceUpdatedEvent(String userId, String status, Instant timestamp) {
        this.userId = userId;
        this.status = status;
        this.timestamp = timestamp;
    }

    public String getUserId() { return userId; }
    public String getStatus() { return status; }
    public Instant getTimestamp() { return timestamp; }
}
