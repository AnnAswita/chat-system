package com.chatapp.chatsystemeda.infrastructure.kafka;

import com.chatapp.chatsystemeda.events.*;
import com.chatapp.chatsystemeda.eventhandlers.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class EventSubscriber {

    private final ObjectMapper objectMapper;

    private final MessageDeliveryHandler messageDeliveryHandler;
    private final PresenceHandler presenceHandler;
    private final LoggingHandler loggingHandler;
    private final HistoryPersistenceHandler historyPersistenceHandler;

    //  Dedicated thread pool for async event handling
    private final ExecutorService eventExecutor = Executors.newFixedThreadPool(16);

    public EventSubscriber(
            ObjectMapper objectMapper,
            MessageDeliveryHandler messageDeliveryHandler,
            PresenceHandler presenceHandler,
            LoggingHandler loggingHandler,
            HistoryPersistenceHandler historyPersistenceHandler
    ) {
        this.objectMapper = objectMapper;
        this.messageDeliveryHandler = messageDeliveryHandler;
        this.presenceHandler = presenceHandler;
        this.loggingHandler = loggingHandler;
        this.historyPersistenceHandler = historyPersistenceHandler;
    }

    // CONCURRENCY ADDED
    @KafkaListener(topics = "MessageSentEvent", groupId = "chat-consumers", concurrency = "8")
    public void onMessageSent(String payload) throws Exception {
        MessageSentEvent event = objectMapper.readValue(payload, MessageSentEvent.class);
        loggingHandler.onAnyEvent(payload);

        //OFFLOAD heavy work to thread pool
        eventExecutor.submit(() -> {
            messageDeliveryHandler.onMessageSent(event);
        });
    }

    @KafkaListener(topics = "MessageDeliveredEvent", groupId = "chat-consumers", concurrency = "4")
    public void onMessageDelivered(String payload) throws Exception {
        MessageDeliveredEvent event = objectMapper.readValue(payload, MessageDeliveredEvent.class);
        loggingHandler.onAnyEvent(payload);

        eventExecutor.submit(() -> {
            historyPersistenceHandler.onMessageDelivered(event);
        });
    }

    @KafkaListener(topics = "UserAuthenticatedEvent", groupId = "chat-consumers", concurrency = "4")
    public void onUserAuthenticated(String payload) throws Exception {
        UserAuthenticatedEvent event = objectMapper.readValue(payload, UserAuthenticatedEvent.class);
        loggingHandler.onAnyEvent(payload);
    }

    @KafkaListener(topics = "UserJoinedRoomEvent", groupId = "chat-consumers", concurrency = "4")
    public void onUserJoinedRoom(String payload) throws Exception {
        UserJoinedRoomEvent event = objectMapper.readValue(payload, UserJoinedRoomEvent.class);
        loggingHandler.onAnyEvent(payload);
    }

    @KafkaListener(topics = "UserLeftRoomEvent", groupId = "chat-consumers", concurrency = "4")
    public void onUserLeftRoom(String payload) throws Exception {
        UserLeftRoomEvent event = objectMapper.readValue(payload, UserLeftRoomEvent.class);
        loggingHandler.onAnyEvent(payload);
    }

    @KafkaListener(topics = "PresenceUpdatedEvent", groupId = "chat-consumers", concurrency = "4")
    public void onPresenceUpdated(String payload) throws Exception {
        PresenceUpdatedEvent event = objectMapper.readValue(payload, PresenceUpdatedEvent.class);
        loggingHandler.onAnyEvent(payload);

        eventExecutor.submit(() -> {
            presenceHandler.onPresenceUpdated(event);
        });
    }
}
