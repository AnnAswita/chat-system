package com.chatapp.chatsystemlayered.integration;

import com.chatapp.chatsystemlayered.domain.chat.ChatRoom;
import com.chatapp.chatsystemlayered.domain.repository.ChatRepository;
import com.chatapp.chatsystemlayered.presentation.dto.ChatMessageDTO;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.reflect.Type;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class WebSocketIntegrationTest {

    private WebSocketStompClient stompClient;

    @Autowired
    private ChatRepository chatRepository;

    @LocalServerPort
    private int localServerPort;

    @BeforeEach
    void setup() {
        stompClient = new WebSocketStompClient(new StandardWebSocketClient());
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
        // Create the room
        ChatRoom room = new ChatRoom("room1");
        chatRepository.save(room);
    }

    @Test
    void testWebSocketMessageBroadcast() throws Exception {

        CompletableFuture<ChatMessageDTO> future = new CompletableFuture<>();

        StompSession session = stompClient.connect(
                "ws://localhost:" + localServerPort + "/ws-chat/websocket",
                new StompSessionHandlerAdapter() {}
        ).get(2, TimeUnit.SECONDS);

        session.subscribe("/topic/rooms/room1", new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return ChatMessageDTO.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                future.complete((ChatMessageDTO) payload);
            }
        });

        ChatMessageDTO dto = new ChatMessageDTO("room1", "ann", "Hello WS", System.currentTimeMillis());

        session.send("/app/chat.send", dto);

        ChatMessageDTO received = future.get(3, TimeUnit.SECONDS);

        assertEquals("Hello WS", received.getContent());
        assertEquals("ann", received.getSenderId());
        assertEquals("room1", received.getRoomId());
    }
}
