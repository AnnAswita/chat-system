package com.chatapp.chatsystemeda.events;

import java.time.Instant;

public class UserJoinedRoomEvent {

    private String userId;
    private String roomId;
    private Instant timestamp;

    public UserJoinedRoomEvent() {
        // required for Jackson
    }

    public UserJoinedRoomEvent(String userId, String roomId, Instant timestamp) {
        this.userId = userId;
        this.roomId = roomId;
        this.timestamp = timestamp;
    }

    public String getUserId() { return userId; }
    public String getRoomId() { return roomId; }
    public Instant getTimestamp() { return timestamp; }
}

