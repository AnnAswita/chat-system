package com.chatapp.chatsystemlayered.domain.identity;

public class User {
    private final String id;
    private final String username;
    private UserStatus status;

    public User(String id, String username) {
        this.id = id;
        this.username = username;
        this.status = UserStatus.OFFLINE;
    }

    public String getId() { return id; }
    public String getUsername() { return username; }

    public UserStatus getStatus() { return status; }
    public void setStatus(UserStatus status) { this.status = status; }
}