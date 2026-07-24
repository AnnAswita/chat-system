package com.chatapp.chatsystemlayered.application.services;

import com.chatapp.chatsystemlayered.application.commands.JoinRoomCommand;
import com.chatapp.chatsystemlayered.application.commands.SendMessageCommand;
import com.chatapp.chatsystemlayered.application.queries.GetMessagesQuery;
import com.chatapp.chatsystemlayered.domain.chat.ChatRoom;
import com.chatapp.chatsystemlayered.domain.chat.Message;
import com.chatapp.chatsystemlayered.domain.chat.MessageContent;
import com.chatapp.chatsystemlayered.domain.repository.ChatRepository;
import com.chatapp.chatsystemlayered.infrastructure.logging.LoggingService;
import com.chatapp.chatsystemlayered.infrastructure.monitoring.MetricsCollector;
import com.chatapp.chatsystemlayered.presentation.dto.ChatMessageDTO;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatService {
    private final ChatRepository chatRepository;
    private final MessagingService messagingService;
    private final LoggingService loggingService;
    private final MetricsCollector metricsCollector;

    public ChatService(ChatRepository chatRepository,
                       MessagingService messagingService,
                       LoggingService loggingService,
                       MetricsCollector metricsCollector) {

        this.chatRepository = chatRepository;
        this.messagingService = messagingService;
        this.loggingService = loggingService;
        this.metricsCollector = metricsCollector;
    }

    public void joinRoom(JoinRoomCommand command) {
        long start = System.currentTimeMillis();

        ChatRoom room = chatRepository.findById(command.getRoomId())
                .orElseGet(() -> new ChatRoom(command.getRoomId()));

        room.addParticipant(command.getUserId());
        chatRepository.save(room);

        loggingService.logEvent("UserJoinedRoom",
                "User " + command.getUserId() + " joined room " + command.getRoomId());

        metricsCollector.recordLatency("joinRoom.latency", System.currentTimeMillis() - start);
    }

    public void leaveRoom(String roomId, String userId) {
        long start = System.currentTimeMillis();

        chatRepository.findById(roomId).ifPresent(room -> {
            room.removeParticipant(userId);
            chatRepository.save(room);
        });

        loggingService.logEvent("UserLeftRoom",
                "User " + userId + " left room " + roomId);

        metricsCollector.recordLatency("leaveRoom.latency", System.currentTimeMillis() - start);
    }

    public void sendMessage(SendMessageCommand command) {
        long start = System.currentTimeMillis();

        ChatRoom room = chatRepository.findById(command.getRoomId())
                .orElseThrow(() -> new IllegalArgumentException("Room not found"));

        MessageContent content = new MessageContent(command.getContent());
        Message message = new Message(
                room.nextMessageId(),
                command.getSenderId(),
                content,
                Instant.now()
        );

        room.addMessage(message);
        chatRepository.save(room);

        loggingService.logEvent("MessageSent",
                "User " + command.getSenderId() + " sent message to room " + command.getRoomId());

        messagingService.sendToRoom(room.getId(), message);

        metricsCollector.recordLatency("sendMessage.latency", System.currentTimeMillis() - start);
    }

    public List<ChatMessageDTO> getMessages(GetMessagesQuery query) {
        long start = System.currentTimeMillis();

        List<ChatMessageDTO> messages = chatRepository.findById(query.getRoomId())
                .map(ChatRoom::getMessages)
                .orElse(List.of())
                .stream()
                .map(m -> new ChatMessageDTO(
                        query.getRoomId(),
                        m.getSenderId(),
                        m.getContent().getText(),
                        m.getTimestamp().toEpochMilli()
                ))
                .collect(Collectors.toList());

        loggingService.logEvent("GetMessages",
                "Fetched history for room " + query.getRoomId());

        metricsCollector.recordLatency("getMessages.latency", System.currentTimeMillis() - start);

        return messages;
    }
}