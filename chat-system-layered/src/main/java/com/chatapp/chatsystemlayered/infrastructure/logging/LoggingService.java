package com.chatapp.chatsystemlayered.infrastructure.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class LoggingService {

    private final Logger logger = LoggerFactory.getLogger(LoggingService.class);

    public void logEvent(String eventName, String details) {
        logger.info("Event: {} | Details: {}", eventName, details);
    }
}