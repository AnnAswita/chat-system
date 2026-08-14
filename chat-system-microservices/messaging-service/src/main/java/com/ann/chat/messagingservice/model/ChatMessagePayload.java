package com.ann.chat.messagingservice.model;

public class ChatMessagePayload {

    private String room;
    private String sender;
    private String content;
    private long timestamp;

    public ChatMessagePayload() {}

    public ChatMessagePayload(String room, String sender, String content, long timestamp) {
        this.room = room;
        this.sender = sender;
        this.content = content;
        this.timestamp = timestamp;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
