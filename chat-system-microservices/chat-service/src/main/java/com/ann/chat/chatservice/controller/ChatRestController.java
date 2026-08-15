package com.ann.chat.chatservice.controller;

import com.ann.chat.chatservice.domain.ChatMessage;
import com.ann.chat.chatservice.infrastructure.ChatMessageRepository;
import com.ann.chat.chatservice.metrics.MetricsConfig;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ChatRestController {

    private final ChatMessageRepository repo;

    @Autowired
    private MetricsConfig metrics;

    public ChatRestController(ChatMessageRepository repo) {
        this.repo = repo;
    }

    @PostMapping("/messages")
    public ChatMessage saveMessage(@RequestBody ChatMessage message) {

//        metrics.incrementMessageThroughput();
//        metrics.recordDeliveryLatency(() -> {});

        return repo.save(message);
    }

    @GetMapping("/messages/{room}")
    public List<ChatMessage> history(@PathVariable String room) {
        return repo.findByRoom(room);
    }
}

