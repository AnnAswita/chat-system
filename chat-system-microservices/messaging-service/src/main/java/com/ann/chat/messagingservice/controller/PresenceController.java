package com.ann.chat.messagingservice.controller;

import com.ann.chat.messagingservice.model.PresenceEvent;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class PresenceController {

    @MessageMapping("/presence")
    @SendTo("/presence/updates")
    public PresenceEvent update(PresenceEvent event) {
        return event;
    }
}

