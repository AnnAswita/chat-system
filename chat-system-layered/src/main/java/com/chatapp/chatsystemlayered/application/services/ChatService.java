package com.chatapp.chatsystemlayered.application.services;

import com.chatapp.chatsystemlayered.application.commands.JoinRoomCommand;
import com.chatapp.chatsystemlayered.application.commands.SendMessageCommand;
import com.chatapp.chatsystemlayered.application.queries.GetMessagesQuery;
import com.chatapp.chatsystemlayered.domain.chat.ChatRoom;
import com.chatapp.chatsystemlayered.domain.chat.Message;
import com.chatapp.chatsystemlayered.domain.chat.MessageContent;
import com.chatapp.chatsystemlayered.domain.repository.ChatRepository;
import com.chatapp.chatsystemlayered.infrastructure.logging.LoggingService;
import com.chatapp.chatsystemlayered.presentation.dto.ChatMessageDTO;
import org.springframework.stereotype.Service;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import io.micrometer.core.instrument.Counter;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;
import java.util.concurrent.TimeUnit;

@Service
public class ChatService {

    private final ChatRepository chatRepository;
    private final MessagingService messagingService;
    private final LoggingService loggingService;

    // Micrometer metrics
    private final Timer joinRoomLatency;
    private final Timer leaveRoomLatency;
    private final Timer sendMessageLatency;
    private final Timer getMessagesLatency;

    private final Counter messageThroughput;

    public ChatService(ChatRepository chatRepository,
                       MessagingService messagingService,
                       LoggingService loggingService,
                       MeterRegistry meterRegistry) {

        this.chatRepository = chatRepository;
        this.messagingService = messagingService;
        this.loggingService = loggingService;

        // Timers
        this.joinRoomLatency = Timer.builder("chat.joinRoom.latency")
                .publishPercentileHistogram()
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);

        this.leaveRoomLatency = Timer.builder("chat.leaveRoom.latency")
                .publishPercentileHistogram()
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);

        this.sendMessageLatency = Timer.builder("chat.sendMessage.latency")
                .publishPercentileHistogram()
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);

        this.getMessagesLatency = Timer.builder("chat.getMessages.latency")
                .publishPercentileHistogram()
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);

        // Throughput counter
        this.messageThroughput = Counter.builder("chat.message.throughput")
                .description("Number of messages processed")
                .register(meterRegistry);
    }
    public void createRoom(String roomId) {
        chatRepository.save(new ChatRoom(roomId));
    }

    public void joinRoom(JoinRoomCommand command) {
        long start = System.nanoTime();

        ChatRoom room = chatRepository.findById(command.getRoomId())
                .orElseGet(() -> new ChatRoom(command.getRoomId()));

        room.addParticipant(command.getUserId());
        chatRepository.save(room);

        loggingService.logEvent("UserJoinedRoom",
                "User " + command.getUserId() + " joined room " + command.getRoomId());

        joinRoomLatency.record(System.nanoTime() - start, TimeUnit.NANOSECONDS);
    }

    public void leaveRoom(String roomId, String userId) {
        long start = System.nanoTime();

        chatRepository.findById(roomId).ifPresent(room -> {
            room.removeParticipant(userId);
            chatRepository.save(room);
        });

        loggingService.logEvent("UserLeftRoom",
                "User " + userId + " left room " + roomId);

        leaveRoomLatency.record(System.nanoTime() - start, TimeUnit.NANOSECONDS);
    }

    public void sendMessage(SendMessageCommand command) {
        long start = System.nanoTime();

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

        // Throughput
        messageThroughput.increment();

        // Latency
        sendMessageLatency.record(System.nanoTime() - start, TimeUnit.NANOSECONDS);
    }

    public List<ChatMessageDTO> getMessages(GetMessagesQuery query) {
        long start = System.nanoTime();

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

        getMessagesLatency.record(System.nanoTime() - start, TimeUnit.NANOSECONDS);

        return messages;
    }
}
