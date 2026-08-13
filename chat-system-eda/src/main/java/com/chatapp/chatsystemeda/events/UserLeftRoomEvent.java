package com.chatapp.chatsystemeda.events;

import java.time.Instant;

public class UserLeftRoomEvent {

    private String userId;
    private String roomId;
    private Instant timestamp;

    public UserLeftRoomEvent() {
        // required for Jackson
    }
    public UserLeftRoomEvent(String userId, String roomId, Instant timestamp) {
        this.userId = userId;
        this.roomId = roomId;
        this.timestamp = timestamp;
    }

    public String getUserId() { return userId; }
    public String getRoomId() { return roomId; }
    public Instant getTimestamp() { return timestamp; }
}
