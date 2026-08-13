package com.chatapp.chatsystemeda.commandhandlers;

import com.chatapp.chatsystemeda.commandbus.CommandHandler;
import com.chatapp.chatsystemeda.commands.SendMessageCommand;
import com.chatapp.chatsystemeda.domain.messaging.Message;
import com.chatapp.chatsystemeda.events.MessageSentEvent;
import com.chatapp.chatsystemeda.infrastructure.kafka.EventPublisher;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.concurrent.CompletableFuture;

@Component
public class SendMessageHandler implements CommandHandler<SendMessageCommand> {

    private final EventPublisher eventPublisher;

    public SendMessageHandler(EventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void handle(SendMessageCommand cmd) {

        Message message = new Message(
                cmd.getUserId(),
                cmd.getRoomId(),
                cmd.getContent()
        );

        MessageSentEvent event = new MessageSentEvent(
                message.getId(),
                message.getUserId(),
                message.getRoomId(),
                message.getContent(),
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
