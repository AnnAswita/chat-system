package com.chatapp.chatsystemeda.api;

import com.chatapp.chatsystemeda.commandbus.CommandBus;
import com.chatapp.chatsystemeda.commands.JoinRoomCommand;
import com.chatapp.chatsystemeda.commands.LeaveRoomCommand;
import com.chatapp.chatsystemeda.commands.SendMessageCommand;
import com.chatapp.chatsystemeda.eventhandlers.MessageDeliveryHandler;
import com.chatapp.chatsystemeda.infrastructure.monitoring.EdaMetrics;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.lang.NonNull;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final CommandBus commandBus;
    private final ObjectMapper mapper;
    private final MessageDeliveryHandler deliveryHandler;
    private final EdaMetrics metrics;

    public ChatWebSocketHandler(CommandBus commandBus,
                                ObjectMapper mapper,
                                MessageDeliveryHandler deliveryHandler,
                                EdaMetrics metrics) {
        this.commandBus = commandBus;
        this.mapper = mapper;
        this.deliveryHandler = deliveryHandler;
        this.metrics = metrics;
    }

    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) {
        metrics.incrementSessions();

        // Extract roomId from URL: /ws-chat/{roomId}
        String path = session.getUri().getPath();          // "/ws-chat/room1"
        String roomId = path.replace("/ws-chat/", "");     // "room1"

        // Store roomId in session attributes
        session.getAttributes().put("roomId", roomId);

        // Register session immediately
        deliveryHandler.registerSession(roomId, session);

        System.out.println("WS connected: room=" + roomId + ", session=" + session.getId());

        try {
            session.sendMessage(new TextMessage("{\"type\":\"welcome\"}"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void afterConnectionClosed(@NonNull WebSocketSession session,
                                      @NonNull CloseStatus status) {

        String roomId = (String) session.getAttributes().get("roomId");
        if (roomId != null) {
            deliveryHandler.removeSession(roomId, session);
        }

        metrics.decrementSessions();
        System.out.println("WS closed: session=" + session.getId());
    }

    @Override
    public void handleTextMessage(@NonNull WebSocketSession session,
                                  @NonNull TextMessage message) throws Exception {

        System.out.println("WS message received: " + message.getPayload());
        metrics.incrementMessageThroughput();

        String json = message.getPayload();
        var node = mapper.readTree(json);
        String type = node.get("type").asText();

        ((ObjectNode) node).remove("type");
        json = mapper.writeValueAsString(node);

        switch (type) {
            case "join" -> {
                JoinRoomCommand cmd = mapper.readValue(json, JoinRoomCommand.class);
                String roomId = cmd.getRoomId();

                session.getAttributes().put("roomId", roomId);
                deliveryHandler.registerSession(roomId, session);

                commandBus.dispatch(cmd);
            }
            case "send" -> {
                SendMessageCommand cmd = mapper.readValue(json, SendMessageCommand.class);
                commandBus.dispatch(cmd);
            }
            case "leave" -> {
                LeaveRoomCommand cmd = mapper.readValue(json, LeaveRoomCommand.class);
                String roomId = (String) session.getAttributes().get("roomId");

                if (roomId != null) {
                    deliveryHandler.removeSession(roomId, session);
                }

                commandBus.dispatch(cmd);
            }
            default -> System.out.println("Unknown message type: " + type);
        }
    }
}
