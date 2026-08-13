package com.chatapp.chatsystemeda.infrastructure.monitoring;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class EventMetricsListener {

    private final MeterRegistry registry;

    public EventMetricsListener(MeterRegistry registry) {
        this.registry = registry;
    }

    public void recordEvent(String eventType) {
        registry.counter("events.total", "type", eventType).increment();
    }

    public void recordLatency(String name, long millis) {
        registry.timer("events.latency", "name", name).record(millis, java.util.concurrent.TimeUnit.MILLISECONDS);
    }
}
