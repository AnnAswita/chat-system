package com.chatapp.chatsystemeda.config;

import com.chatapp.chatsystemeda.commandbus.CommandBus;
import com.chatapp.chatsystemeda.commandhandlers.AuthenticateUserHandler;
import com.chatapp.chatsystemeda.commandhandlers.JoinRoomHandler;
import com.chatapp.chatsystemeda.commandhandlers.LeaveRoomHandler;
import com.chatapp.chatsystemeda.commandhandlers.SendMessageHandler;
import com.chatapp.chatsystemeda.commands.AuthenticateUserCommand;
import com.chatapp.chatsystemeda.commands.JoinRoomCommand;
import com.chatapp.chatsystemeda.commands.LeaveRoomCommand;
import com.chatapp.chatsystemeda.commands.SendMessageCommand;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommandBusConfig {

    private final CommandBus commandBus;
    private final SendMessageHandler sendMessageHandler;
    private final JoinRoomHandler joinRoomHandler;
    private final LeaveRoomHandler leaveRoomHandler;
    private final AuthenticateUserHandler authenticateUserHandler;

    public CommandBusConfig(CommandBus commandBus,
                            SendMessageHandler sendMessageHandler,
                            JoinRoomHandler joinRoomHandler,
                            LeaveRoomHandler leaveRoomHandler,
                            AuthenticateUserHandler authenticateUserHandler) {
        this.commandBus = commandBus;
        this.sendMessageHandler = sendMessageHandler;
        this.joinRoomHandler = joinRoomHandler;
        this.leaveRoomHandler = leaveRoomHandler;
        this.authenticateUserHandler = authenticateUserHandler;
    }

    @PostConstruct
    public void registerHandlers() {
        commandBus.register(SendMessageCommand.class, sendMessageHandler);
        commandBus.register(JoinRoomCommand.class, joinRoomHandler);
        commandBus.register(LeaveRoomCommand.class, leaveRoomHandler);
        commandBus.register(AuthenticateUserCommand.class, authenticateUserHandler);
    }
}
