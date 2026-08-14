package com.ann.chat.chatservice.controller;

import com.ann.chat.chatservice.domain.ChatMessage;
import com.ann.chat.chatservice.infrastructure.ChatMessageRepository;
import com.ann.chat.chatservice.metrics.MetricsConfig;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;

@Controller
public class ChatWebSocketController {

    private final ChatMessageRepository repo;

    @Autowired
    private MetricsConfig metrics;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;   // ⭐ REQUIRED

    public ChatWebSocketController(ChatMessageRepository repo) {
        this.repo = repo;
    }

    @MessageMapping("/send")
    public void sendViaWebSocket(ChatMessage message) {

        // Save message in chat-service
        repo.save(message);

        // Count chat-service throughput
//        metrics.incrementMessageThroughput();

        // ⭐ Forward message to messaging-service
        messagingTemplate.convertAndSend("/app/deliver", message);
    }
}
