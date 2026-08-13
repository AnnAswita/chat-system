package com.chatapp.chatsystemeda.eventhandlers;

import com.chatapp.chatsystemeda.events.PresenceUpdatedEvent;
import org.springframework.stereotype.Component;

@Component
public class PresenceHandler {

    public void onPresenceUpdated(PresenceUpdatedEvent event) {
        System.out.println("Presence updated: " + event.getUserId() + " -> " + event.getStatus());
        // In a real system, update Redis presence store
    }
}
