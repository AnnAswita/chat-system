package com.chatapp.chatsystemeda.commandbus;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Component
public class CommandBus {

    private final Map<Class<?>, CommandHandler<?>> handlers = new HashMap<>();

    public <T> void register(Class<T> type, CommandHandler<T> handler) {
        handlers.put(type, handler);
    }

    @SuppressWarnings("unchecked")
    public <T> void dispatch(T command) {
        CommandHandler<T> handler = (CommandHandler<T>) handlers.get(command.getClass());
        if (handler == null) {
            throw new IllegalStateException("No handler for " + command.getClass());
        }

        // ⭐ NON-BLOCKING ASYNC DISPATCH
        CompletableFuture.runAsync(() -> {
            try {
                handler.handle(command);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
