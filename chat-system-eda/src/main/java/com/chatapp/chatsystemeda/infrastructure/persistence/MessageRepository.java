package com.chatapp.chatsystemeda.infrastructure.persistence;

import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public class MessageRepository {

    public void saveDeliveredMessage(String messageId,
                                     String roomId,
                                     List<String> deliveredTo,
                                     Instant timestamp) {
        // For demo: just print; in real app, persist to Postgres
        System.out.printf(
                "Persisting delivered message: id=%s room=%s deliveredTo=%s at=%s%n",
                messageId, roomId, deliveredTo, timestamp
        );
    }
}
