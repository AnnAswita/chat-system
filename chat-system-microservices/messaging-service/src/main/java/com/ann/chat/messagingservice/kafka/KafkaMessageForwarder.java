package com.ann.chat.messagingservice.kafka;

import com.ann.chat.messagingservice.model.ChatMessagePayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaMessageForwarder {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @KafkaListener(topics = "chat-messages", groupId = "messaging-service")
    public void forward(ChatMessagePayload payload) {
        messagingTemplate.convertAndSend("/topic/messages", payload);
    }
}
