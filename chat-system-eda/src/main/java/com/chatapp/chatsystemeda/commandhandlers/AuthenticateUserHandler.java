package com.chatapp.chatsystemeda.commandhandlers;

import com.chatapp.chatsystemeda.commandbus.CommandHandler;
import com.chatapp.chatsystemeda.commands.AuthenticateUserCommand;
import com.chatapp.chatsystemeda.events.UserAuthenticatedEvent;
import com.chatapp.chatsystemeda.infrastructure.kafka.EventPublisher;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
public class AuthenticateUserHandler implements CommandHandler<AuthenticateUserCommand> {

    private final EventPublisher eventPublisher;

    public AuthenticateUserHandler(EventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void handle(AuthenticateUserCommand cmd) {
        // demo: accept any user, generate fake token
        String token = UUID.randomUUID().toString();

        UserAuthenticatedEvent event = new UserAuthenticatedEvent(
                cmd.getUsername(),
                token,
                Instant.now()
        );

        // publish synchronously (authentication is not part of load test)
        eventPublisher.publish(event);

        // store token in the command so controller can read it
        cmd.setGeneratedToken(token);
    }
}
