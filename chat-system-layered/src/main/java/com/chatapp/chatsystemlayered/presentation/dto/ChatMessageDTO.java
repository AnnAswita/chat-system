package com.chatapp.chatsystemlayered.presentation.dto;

public class ChatMessageDTO {
    private String roomId;
    private String senderId;
    private String content;
    private long timestamp;

    public ChatMessageDTO() {}

    public ChatMessageDTO(String roomId, String senderId, String content, long timestamp) {
        this.roomId = roomId;
        this.senderId = senderId;
        this.content = content;
        this.timestamp = timestamp;
    }

    public String getRoomId() { return roomId; }
    public void setRoomId(String roomId) { this.roomId = roomId; }

    public String getSenderId() { return senderId; }
    public void setSenderId(String senderId) { this.senderId = senderId; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}
