package com.chatapp.chatsystemlayered.domain.chat;

import java.util.*;

public class ChatRoom {
    private final String id;
    private final Set<String> participants = new HashSet<>();
    private final List<Message> messages = new ArrayList<>();
    private long messageSequence = 0L;

    public ChatRoom(String id) {
        this.id = id;
    }

    public String getId() { return id; }

    public void addParticipant(String userId) {
        participants.add(userId);
    }

    public void removeParticipant(String userId) {
        participants.remove(userId);
    }

    public void addMessage(Message message) {
        messages.add(message);
    }

    public List<Message> getMessages() {
        return Collections.unmodifiableList(messages);
    }

    public long nextMessageId() {
        return ++messageSequence;
    }
}
