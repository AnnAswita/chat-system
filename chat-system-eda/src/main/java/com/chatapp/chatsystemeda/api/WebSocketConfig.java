package com.chatapp.chatsystemeda.api;

import com.chatapp.chatsystemeda.commandbus.CommandBus;
import com.chatapp.chatsystemeda.eventhandlers.MessageDeliveryHandler;
import com.chatapp.chatsystemeda.infrastructure.monitoring.EdaMetrics;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final CommandBus commandBus;
    private final ObjectMapper mapper;
    private final MessageDeliveryHandler deliveryHandler;
    private final EdaMetrics metrics;

    public WebSocketConfig(CommandBus commandBus,
                           ObjectMapper mapper,
                           MessageDeliveryHandler deliveryHandler,
                           EdaMetrics metrics) {
        this.commandBus = commandBus;
        this.mapper = mapper;
        this.deliveryHandler = deliveryHandler;
        this.metrics = metrics;
    }

    @Bean
    public ChatWebSocketHandler chatWebSocketHandler() {
        return new ChatWebSocketHandler(commandBus, mapper, deliveryHandler, metrics);
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {

        registry.addHandler(chatWebSocketHandler(), "/ws-chat/*")
                .setAllowedOrigins("*");
    }
}
