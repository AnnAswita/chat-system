package com.chatapp.chatsystemlayered.infrastructure.websocket;

import com.chatapp.chatsystemlayered.application.services.MessagingService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
public class WebSocketEventListener {

    private final MessagingService messagingService;

    public WebSocketEventListener(MessagingService messagingService) {
        this.messagingService = messagingService;
    }

    @EventListener
    public void handleSessionConnected(SessionConnectEvent event) {
        messagingService.registerSession(event.getMessage().getHeaders().get("simpSessionId").toString());
    }

    @EventListener
    public void handleSessionDisconnect(SessionDisconnectEvent event) {
        messagingService.removeSession(event.getSessionId());
    }
}
