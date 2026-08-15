package com.ann.chat.messagingservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Autowired
    private ThreadPoolTaskScheduler brokerTaskScheduler;
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {

        // ⭐ Enable broker with heartbeats (critical for high load)
        registry.enableSimpleBroker("/topic", "/presence")
                .setHeartbeatValue(new long[]{10000, 10000})
                .setTaskScheduler(brokerTaskScheduler);

        registry.setApplicationDestinationPrefixes("/app");

        // ⭐ Prevent memory blow-up under load
        registry.setCacheLimit(1024); // 1 KB per destination
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {

        registry.addEndpoint("/ws-messaging")
                .setAllowedOriginPatterns("*")
                .withSockJS(); // SockJS fallback
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {

        // ⭐ Increase inbound processing capacity
        registration.taskExecutor()
                .corePoolSize(50)
                .maxPoolSize(200)
                .keepAliveSeconds(60);
    }

    @Override
    public void configureClientOutboundChannel(ChannelRegistration registration) {

        // ⭐ Increase outbound processing capacity
        registration.taskExecutor()
                .corePoolSize(50)
                .maxPoolSize(200)
                .keepAliveSeconds(60);
    }
}
