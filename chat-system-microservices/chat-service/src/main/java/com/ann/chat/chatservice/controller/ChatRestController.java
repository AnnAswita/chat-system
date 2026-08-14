package com.ann.chat.chatservice.controller;

import com.ann.chat.chatservice.domain.ChatMessage;
import com.ann.chat.chatservice.infrastructure.ChatMessageRepository;
import com.ann.chat.chatservice.metrics.MetricsConfig;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@RestController
@RequestMapping("/chat")
public class ChatRestController {

    private final ChatMessageRepository repo;

    @Autowired
    private MetricsConfig metrics;

    public ChatRestController(ChatMessageRepository repo) {
        this.repo = repo;
    }

    @PostMapping("/send")
    public ChatMessage sendViaRest(@RequestBody ChatMessage message) {
        return repo.save(message);
    }

    @GetMapping("/{room}")
    public List<ChatMessage> history(@PathVariable String room) {
        return repo.findByRoom(room);
    }
}
