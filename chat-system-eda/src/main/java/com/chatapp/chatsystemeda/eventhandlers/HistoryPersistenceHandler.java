package com.chatapp.chatsystemeda.eventhandlers;

import com.chatapp.chatsystemeda.events.MessageDeliveredEvent;
import com.chatapp.chatsystemeda.infrastructure.persistence.MessageRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class HistoryPersistenceHandler {

    private final MessageRepository messageRepository;

    public HistoryPersistenceHandler(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public void onMessageDelivered(MessageDeliveredEvent event) {
        // Persist message delivery info
        messageRepository.saveDeliveredMessage(
                event.getMessageId(),
                event.getRoomId(),
                event.getDeliveredTo(),
                event.getTimestamp()
        );
    }
}
