package com.chatapp.chatsystemeda.commands;

public class LeaveRoomCommand {
    private String userId;
    private String roomId;

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getRoomId() { return roomId; }
    public void setRoomId(String roomId) { this.roomId = roomId; }
}
