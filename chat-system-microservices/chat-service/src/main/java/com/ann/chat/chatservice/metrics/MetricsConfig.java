package com.ann.chat.chatservice.metrics;

import io.micrometer.core.instrument.*;
import io.micrometer.core.instrument.binder.jvm.*;
import io.micrometer.core.instrument.binder.system.*;
import io.micrometer.core.instrument.binder.http.*;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.context.event.EventListener;
import org.springframework.web.socket.messaging.SessionConnectEvent;

@Configuration
public class MetricsConfig {

    private final MeterRegistry registry;
    private final SimpUserRegistry userRegistry;

    private Counter messageThroughput;
    private Timer deliveryLatency;
    private Counter stompConnections;

    public MetricsConfig(MeterRegistry registry, SimpUserRegistry userRegistry) {
        this.registry = registry;
        this.userRegistry = userRegistry;
    }

    @PostConstruct
    public void init() {

        // JVM + System Metrics
        new JvmMemoryMetrics().bindTo(registry);
        new JvmGcMetrics().bindTo(registry);
        new ProcessorMetrics().bindTo(registry);
        new ClassLoaderMetrics().bindTo(registry);
        new FileDescriptorMetrics().bindTo(registry);

    }


}
