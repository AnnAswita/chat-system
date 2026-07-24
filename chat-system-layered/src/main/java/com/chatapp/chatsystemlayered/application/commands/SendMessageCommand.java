package com.chatapp.chatsystemlayered.application.commands;

public class SendMessageCommand {
    private final String roomId;
    private final String senderId;
    private final String content;

    public SendMessageCommand(String roomId, String senderId, String content) {
        this.roomId = roomId;
        this.senderId = senderId;
        this.content = content;
    }

    public String getRoomId() { return roomId; }
    public String getSenderId() { return senderId; }
    public String getContent() { return content; }
}
