package com.chatapp.chatsystemeda.events;

import java.time.Instant;
import java.util.List;

public class MessageDeliveredEvent {

    private String messageId;
    private String roomId;
    private List<String> deliveredTo;
    private Instant timestamp;
    public MessageDeliveredEvent(){}
    public MessageDeliveredEvent(String messageId, String roomId, List<String> deliveredTo, Instant timestamp) {
        this.messageId = messageId;
        this.roomId = roomId;
        this.deliveredTo = deliveredTo;
        this.timestamp = timestamp;
    }

    public String getMessageId() { return messageId; }
    public String getRoomId() { return roomId; }
    public List<String> getDeliveredTo() { return deliveredTo; }
    public Instant getTimestamp() { return timestamp; }
}
