package com.chatapp.chatsystemlayered.presentation.controllers;

import com.chatapp.chatsystemlayered.application.services.ChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    private final ChatService chatService;

    public RoomController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/{roomId}")
    public ResponseEntity<Void> createRoom(@PathVariable String roomId) {
        chatService.createRoom(roomId);
        return ResponseEntity.ok().build();
    }
}
