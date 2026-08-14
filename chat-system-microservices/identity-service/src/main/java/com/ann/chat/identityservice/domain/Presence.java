package com.ann.chat.identityservice.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Presence {

    @Id
    private String userId;
    private boolean online;

    public Presence() {}

    public Presence(String userId, boolean online) {
        this.userId = userId;
        this.online = online;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }
}
