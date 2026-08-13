package com.chatapp.chatsystemeda.commands;

public class SendMessageCommand {
    private String userId;
    private String roomId;
    private String content;

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getRoomId() { return roomId; }
    public void setRoomId(String roomId) { this.roomId = roomId; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}
