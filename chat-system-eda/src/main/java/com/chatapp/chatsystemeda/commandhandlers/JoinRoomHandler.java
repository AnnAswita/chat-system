package com.chatapp.chatsystemeda.commandhandlers;

import com.chatapp.chatsystemeda.commandbus.CommandHandler;
import com.chatapp.chatsystemeda.commands.JoinRoomCommand;
import com.chatapp.chatsystemeda.events.UserJoinedRoomEvent;
import com.chatapp.chatsystemeda.infrastructure.kafka.EventPublisher;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.concurrent.CompletableFuture;

@Component
public class JoinRoomHandler implements CommandHandler<JoinRoomCommand> {

    private final EventPublisher eventPublisher;

    public JoinRoomHandler(EventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void handle(JoinRoomCommand cmd) {

        UserJoinedRoomEvent event = new UserJoinedRoomEvent(
                cmd.getUserId(),
                cmd.getRoomId(),
                Instant.now()
        );

        // ⭐ NON-BLOCKING KAFKA PUBLISH
        CompletableFuture.runAsync(() -> {
            try {
                eventPublisher.publish(event);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
