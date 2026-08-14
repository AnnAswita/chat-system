package com.ann.chat.messagingservice.controller;

import com.ann.chat.messagingservice.metrics.MetricsConfig;
import com.ann.chat.messagingservice.model.ChatMessagePayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
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

    @MessageMapping("/deliver")
    public void deliver(ChatMessagePayload payload) {
        log.info("Delivering message from {} in room {}", payload.getSender(), payload.getRoom());

        // Delivery latency metric
        metrics.recordDeliveryLatency(() -> {
            messagingTemplate.convertAndSend("/topic/messages", payload);
        });

        //  Throughput metric
        metrics.incrementMessageThroughput();
    }
}
