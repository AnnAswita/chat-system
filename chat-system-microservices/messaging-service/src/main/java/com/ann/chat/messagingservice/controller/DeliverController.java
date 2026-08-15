package com.ann.chat.messagingservice.controller;

import com.ann.chat.messagingservice.metrics.MetricsConfig;
import com.ann.chat.messagingservice.model.ChatMessagePayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class DeliverController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private MetricsConfig metrics;

    private static final Logger log = LoggerFactory.getLogger(DeliverController.class);

    @Async("messageExecutor") // ⭐ Non-blocking delivery
    @MessageMapping("/deliver")
    public void deliver(ChatMessagePayload payload) {

        log.debug("Delivering message from {} in room {}", payload.getSender(), payload.getRoom());

        metrics.recordDeliveryLatency(() -> {
            messagingTemplate.convertAndSend("/topic/messages", payload);
        });

        metrics.incrementMessageThroughput();
    }
}
