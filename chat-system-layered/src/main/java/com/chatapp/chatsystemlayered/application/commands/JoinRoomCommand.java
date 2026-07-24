package com.chatapp.chatsystemlayered.application.commands;

public class JoinRoomCommand {
    private final String roomId;
    private final String userId;

    public JoinRoomCommand(String roomId, String userId) {
        this.roomId = roomId;
        this.userId = userId;
    }

    public String getRoomId() { return roomId; }
    public String getUserId() { return userId; }
}