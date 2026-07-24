package com.chatapp.chatsystemlayered.application.queries;

public class GetMessagesQuery {
    private final String roomId;

    public GetMessagesQuery(String roomId) {
        this.roomId = roomId;
    }

    public String getRoomId() { return roomId; }
}