package com.chatapp.chatsystemlayered.presentation.controllers;

import com.chatapp.chatsystemlayered.application.commands.JoinRoomCommand;
import com.chatapp.chatsystemlayered.application.commands.SendMessageCommand;
import com.chatapp.chatsystemlayered.application.queries.GetMessagesQuery;
import com.chatapp.chatsystemlayered.application.services.ChatService;
import com.chatapp.chatsystemlayered.presentation.dto.ChatMessageDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/join/{roomId}")
    public ResponseEntity<Void> joinRoom(@PathVariable String roomId,
                                         @RequestHeader("X-User-Id") String userId) {
        chatService.joinRoom(new JoinRoomCommand(roomId, userId));
        return ResponseEntity.ok().build();
    }

    @PostMapping("/leave/{roomId}")
    public ResponseEntity<Void> leaveRoom(@PathVariable String roomId,
                                          @RequestHeader("X-User-Id") String userId) {
        chatService.leaveRoom(roomId, userId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/send/{roomId}")
    public ResponseEntity<Void> sendMessage(@PathVariable String roomId,
                                            @RequestHeader("X-User-Id") String userId,
                                            @RequestBody ChatMessageDTO dto) {
        chatService.sendMessage(new SendMessageCommand(roomId, userId, dto.getContent()));
        return ResponseEntity.ok().build();
    }

    @GetMapping("/history/{roomId}")
    public ResponseEntity<List<ChatMessageDTO>> getHistory(@PathVariable String roomId) {
        List<ChatMessageDTO> messages = chatService.getMessages(new GetMessagesQuery(roomId));
        return ResponseEntity.ok(messages);
    }
}
