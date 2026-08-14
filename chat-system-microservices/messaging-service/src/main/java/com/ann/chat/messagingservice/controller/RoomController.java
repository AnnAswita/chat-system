package com.ann.chat.messagingservice.controller;

import com.ann.chat.messagingservice.domain.ChatRoom;
import com.ann.chat.messagingservice.infrastructure.ChatRoomRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rooms")
public class RoomController {

    private final ChatRoomRepository repo;

    public RoomController(ChatRoomRepository repo) {
        this.repo = repo;
    }

    @PostMapping
    public ChatRoom create(@RequestBody ChatRoom room) {
        return repo.save(room);
    }

    @GetMapping
    public List<ChatRoom> list() {
        return repo.findAll();
    }
}
