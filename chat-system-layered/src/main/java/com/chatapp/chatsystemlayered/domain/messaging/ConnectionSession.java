package com.chatapp.chatsystemlayered.domain.messaging;

import java.time.Instant;

public class ConnectionSession {
    private final String sessionId;
    private final String userId;
    private final Instant connectedAt;

    public ConnectionSession(String sessionId, String userId, Instant connectedAt) {
        this.sessionId = sessionId;
        this.userId = userId;
        this.connectedAt = connectedAt;
    }

    public String getSessionId() { return sessionId; }
    public String getUserId() { return userId; }
    public Instant getConnectedAt() { return connectedAt; }
}