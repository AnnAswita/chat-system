package com.chatapp.chatsystemeda.domain.chat;

import java.util.HashSet;
import java.util.Set;

public class ChatRoom {

    private final String roomId;
    private final Set<String> members = new HashSet<>();

    public ChatRoom(String roomId) {
        this.roomId = roomId;
    }

    public String getRoomId() { return roomId; }

    public void addMember(String userId) {
        members.add(userId);
    }

    public void removeMember(String userId) {
        members.remove(userId);
    }

    public boolean hasMember(String userId) {
        return members.contains(userId);
    }

    public Set<String> getMembers() {
        return members;
    }
}
