package com.chatapp.chatsystemeda.infrastructure.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class EventPublisher {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public EventPublisher(KafkaTemplate<String, String> kafkaTemplate,
                          ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper; // <-- use Spring-managed mapper
    }

    public void publish(Object event) {
        try {
            String topic = event.getClass().getSimpleName();
            String payload = objectMapper.writeValueAsString(event);
            kafkaTemplate.send(topic, payload);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
