package com.chatapp.chatsystemeda.eventhandlers;

import com.chatapp.chatsystemeda.events.MessageDeliveredEvent;
import com.chatapp.chatsystemeda.events.MessageSentEvent;
import com.chatapp.chatsystemeda.infrastructure.kafka.EventPublisher;
import com.chatapp.chatsystemeda.infrastructure.monitoring.EdaMetrics;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

@Component
public class MessageDeliveryHandler {

    private final Map<String, Map<String, WebSocketSession>> rooms = new ConcurrentHashMap<>();
    private final ObjectMapper mapper;
    private final EventPublisher eventPublisher;
    private final EdaMetrics metrics;

    //  Dedicated thread pool for WebSocket broadcast
    private final ExecutorService broadcastPool = Executors.newFixedThreadPool(32);

    public MessageDeliveryHandler(ObjectMapper mapper,
                                  EventPublisher eventPublisher,
                                  EdaMetrics metrics) {
        this.mapper = mapper;
        this.eventPublisher = eventPublisher;
        this.metrics = metrics;
    }

    public void registerSession(String roomId, WebSocketSession session) {
        rooms.computeIfAbsent(roomId, r -> new ConcurrentHashMap<>())
                .put(session.getId(), session);
    }

    public void removeSession(String roomId, WebSocketSession session) {
        var map = rooms.get(roomId);
        if (map != null) {
            map.remove(session.getId());
        }
    }

    public void onMessageSent(MessageSentEvent event) {
        var sessions = rooms.get(event.getRoomId());

        if (sessions != null) {
            try {
                String json = mapper.writeValueAsString(event);

                // Broadcast asynchronously — no blocking inside Kafka listener
                for (WebSocketSession session : sessions.values()) {
                    if (session.isOpen()) {
                        broadcastPool.submit(() -> {
                            try {
                                session.sendMessage(new TextMessage(json));
                                metrics.incrementBroadcastDeliveries(1);
                            } catch (Exception ignored) {}
                        });
                    }
                }
                metrics.recordDeliveryLatency(0.000001);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Publish MessageDeliveredEvent asynchronously
        CompletableFuture.runAsync(() -> {
            MessageDeliveredEvent deliveredEvent = new MessageDeliveredEvent(
                    event.getMessageId(),
                    event.getRoomId(),
                    List.of("demoUser"),
                    Instant.now()
            );

            eventPublisher.publish(deliveredEvent);
        });
    }
}
