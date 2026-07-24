package com.chatapp.chatsystemlayered.presentation.controllers;
import com.chatapp.chatsystemlayered.application.commands.SendMessageCommand;
import com.chatapp.chatsystemlayered.application.services.ChatService;
import com.chatapp.chatsystemlayered.presentation.dto.ChatMessageDTO;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    private final ChatService chatService;

    public WebSocketController(ChatService chatService) {
        this.chatService = chatService;
    }

    @MessageMapping("/chat.send")
    public void receiveMessage(@Payload ChatMessageDTO dto) {
        chatService.sendMessage(
                new SendMessageCommand(dto.getRoomId(), dto.getSenderId(), dto.getContent())
        );
    }
}
