package com.chatapp.chatsystemeda.events;

import java.time.Instant;

public class UserAuthenticatedEvent {

    private String username;
    private String token;
    private Instant timestamp;


    public UserAuthenticatedEvent() {
        // required for Jackson
    }
    public UserAuthenticatedEvent(String username, String token, Instant timestamp) {
        this.username = username;
        this.token = token;
        this.timestamp = timestamp;
    }

    public String getUsername() { return username; }
    public String getToken() { return token; }
    public Instant getTimestamp() { return timestamp; }
}
