package com.ann.chat.messagingservice.controller;

import com.ann.chat.messagingservice.metrics.MetricsConfig;
import com.ann.chat.messagingservice.model.ChatMessagePayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.stereotype.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class DeliverController {

    @Autowired
    private KafkaTemplate<String, ChatMessagePayload> kafkaTemplate;

    @Autowired
    private MetricsConfig metrics;

    private final WebClient chatServiceClient = WebClient.builder()
            .baseUrl("http://192.168.178.194:8082")
            .build();


    private static final Logger log = LoggerFactory.getLogger(DeliverController.class);

    @MessageMapping("/deliver")
    public void deliver(ChatMessagePayload payload) {
        // Throughput (count once per incoming message)
        metrics.incrementMessageThroughput();
        metrics.recordDeliveryLatency(() -> {
            log.debug("Delivering message from {} in room {}", payload.getSender(), payload.getRoom());

            // 1. REST → Chat-Service latency
            metrics.recordRestCallLatency(() -> {
                chatServiceClient.post()
                        .uri("/api/messages")
                        .bodyValue(payload)
                        .retrieve()
                        .bodyToMono(ChatMessagePayload.class)
                        .block();
            });

            // 2. Kafka publish latency
            metrics.recordKafkaPublishLatency(() -> {
                kafkaTemplate.send("chat-messages", payload.getRoom(), payload);
            });


        });
    }
}
