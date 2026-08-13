package com.chatapp.chatsystemeda.commandhandlers;

import com.chatapp.chatsystemeda.commandbus.CommandHandler;
import com.chatapp.chatsystemeda.commands.LeaveRoomCommand;
import com.chatapp.chatsystemeda.events.UserLeftRoomEvent;
import com.chatapp.chatsystemeda.infrastructure.kafka.EventPublisher;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.concurrent.CompletableFuture;

@Component
public class LeaveRoomHandler implements CommandHandler<LeaveRoomCommand> {

    private final EventPublisher eventPublisher;

    public LeaveRoomHandler(EventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void handle(LeaveRoomCommand cmd) {

        UserLeftRoomEvent event = new UserLeftRoomEvent(
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
