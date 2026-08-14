package com.ann.chat.chatservice.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class ChatMessage {

    @Id
    @GeneratedValue
    private Long id;

    private String sender;
    private String content;
    private String room;

    public ChatMessage() {}

    public ChatMessage(String sender, String content, String room) {
        this.sender = sender;
        this.content = content;
        this.room = room;
    }

    public Long getId() { return id; }
    public String getSender() { return sender; }
    public String getContent() { return content; }
    public String getRoom() { return room; }

    public void setSender(String sender) { this.sender = sender; }
    public void setContent(String content) { this.content = content; }
    public void setRoom(String room) { this.room = room; }
}
