package com.chatapp.chatsystemlayered.application.services;

import com.chatapp.chatsystemlayered.domain.chat.Message;
import com.chatapp.chatsystemlayered.infrastructure.logging.LoggingService;
import com.chatapp.chatsystemlayered.infrastructure.monitoring.MetricsCollector;
import com.chatapp.chatsystemlayered.infrastructure.websocket.WebSocketHandler;
import org.springframework.stereotype.Service;

@Service
public class MessagingService {

    private final WebSocketHandler webSocketHandler;
    private final LoggingService loggingService;
    private final MetricsCollector metricsCollector;

    public MessagingService(WebSocketHandler webSocketHandler,
                            LoggingService loggingService,
                            MetricsCollector metricsCollector) {

        this.webSocketHandler = webSocketHandler;
        this.loggingService = loggingService;
        this.metricsCollector = metricsCollector;
    }

    public void sendToRoom(String roomId, Message message) {
        long start = System.currentTimeMillis();

        webSocketHandler.broadcastToRoom(roomId, message);

        loggingService.logEvent("MessageDelivered",
                "Delivered message " + message.getId() + " to room " + roomId);

        metricsCollector.recordLatency("deliverMessage.latency", System.currentTimeMillis() - start);
    }
}