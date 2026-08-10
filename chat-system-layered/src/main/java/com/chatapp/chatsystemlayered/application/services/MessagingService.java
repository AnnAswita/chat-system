package com.chatapp.chatsystemlayered.application.services;

import com.chatapp.chatsystemlayered.domain.chat.Message;
import com.chatapp.chatsystemlayered.infrastructure.logging.LoggingService;
import com.chatapp.chatsystemlayered.presentation.dto.ChatMessageDTO;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import io.micrometer.core.instrument.Gauge;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Service
public class MessagingService {

    private final LoggingService loggingService;
    private final SimpMessagingTemplate messagingTemplate;

    // Track active WebSocket sessions
    private final Map<String, Object> activeSessions = new ConcurrentHashMap<>();

    // Micrometer latency timer
    private final Timer deliverMessageLatency;

    public MessagingService(LoggingService loggingService,
                            MeterRegistry meterRegistry,
                            SimpMessagingTemplate messagingTemplate) {

        this.loggingService = loggingService;
        this.messagingTemplate = messagingTemplate;

        // Latency timer for message delivery
        this.deliverMessageLatency = Timer.builder("chat.deliverMessage.latency")
                .description("Latency for delivering messages to WebSocket clients")
                .publishPercentileHistogram()
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);

        // WebSocket session gauge
        Gauge.builder("chat.websocket.sessions", activeSessions::size)
                .description("Number of active WebSocket sessions")
                .register(meterRegistry);
    }

    public void registerSession(String sessionId) {
        activeSessions.put(sessionId, new Object());
    }

    public void removeSession(String sessionId) {
        activeSessions.remove(sessionId);
    }

    public void sendToRoom(String roomId, Message message) {
        long start = System.nanoTime();

        ChatMessageDTO dto = new ChatMessageDTO(
                roomId,
                message.getSenderId(),
                message.getContent().getText(),
                message.getTimestamp().toEpochMilli()
        );

        // STOMP broadcast
        messagingTemplate.convertAndSend("/topic/rooms/" + roomId, dto);

        loggingService.logEvent("MessageDelivered",
                "Delivered message " + message.getId() + " to room " + roomId);

        deliverMessageLatency.record(System.nanoTime() - start, TimeUnit.NANOSECONDS);
    }
}
