package com.ann.chat.messagingservice.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class ChatRoom {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    public ChatRoom() {}

    public ChatRoom(String name) {
        this.name = name;
    }

    public Long getId() { return id; }
    public String getName() { return name; }

    public void setName(String name) { this.name = name; }
}
