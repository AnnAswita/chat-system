package com.chatapp.chatsystemeda.commandbus;

public interface CommandHandler<T> {
    void handle(T command);
}
