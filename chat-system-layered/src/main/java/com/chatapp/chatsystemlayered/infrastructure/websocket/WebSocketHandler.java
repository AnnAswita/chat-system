package com.chatapp.chatsystemlayered.infrastructure.websocket;

import com.chatapp.chatsystemlayered.domain.chat.Message;
import com.chatapp.chatsystemlayered.presentation.dto.ChatMessageDTO;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class WebSocketHandler {

    private final SimpMessagingTemplate messagingTemplate;

    public WebSocketHandler(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void broadcastToRoom(String roomId, Message message) {
        ChatMessageDTO dto = new ChatMessageDTO(
                roomId,
                message.getSenderId(),
                message.getContent().getText(),
                message.getTimestamp().toEpochMilli()
        );
        messagingTemplate.convertAndSend("/topic/rooms/" + roomId, dto);
    }
}
