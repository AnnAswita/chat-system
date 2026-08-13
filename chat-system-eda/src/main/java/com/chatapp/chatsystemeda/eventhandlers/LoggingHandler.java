package com.chatapp.chatsystemeda.eventhandlers;

import org.springframework.stereotype.Component;

@Component
public class LoggingHandler {

    public void onAnyEvent(String payload) {
        System.out.println("[AUDIT LOG] " + payload);
    }
}
