package com.chatapp.chatsystemeda.infrastructure.monitoring;

import io.micrometer.core.instrument.*;
import org.springframework.stereotype.Component;

@Component
public class EdaMetrics {

    private final Gauge websocketSessions;
    private volatile int activeSessions = 0;

    private final Counter messageThroughput;
    private final Counter broadcastDeliveries;

    private final DistributionSummary deliveryLatency;

    public EdaMetrics(MeterRegistry registry) {

        // Active WebSocket Sessions
        this.websocketSessions = Gauge.builder("chat_websocket_sessions", this, EdaMetrics::getActiveSessions)
                .description("Number of active WebSocket connections")
                .register(registry);

        // Message Throughput (messages received)
        this.messageThroughput = Counter.builder("chat_message_throughput_total")
                .description("Total messages received from clients")
                .register(registry);

        // Broadcast Deliveries (messages delivered to subscribers)
        this.broadcastDeliveries = Counter.builder("chat_broadcast_deliveries_total")
                .description("Total message deliveries to subscribers")
                .register(registry);

        // Delivery Latency Histogram
        this.deliveryLatency = DistributionSummary.builder("chat_deliverMessage_latency_seconds")
                .description("Latency of delivering messages to subscribers")
                .publishPercentileHistogram()
                .publishPercentiles(0.95)
                .register(registry);
    }

    // WebSocket session tracking
    public void incrementSessions() {
        activeSessions++;
    }

    public void decrementSessions() {
        activeSessions--;
    }

    public int getActiveSessions() {
        return activeSessions;
    }

    // Message throughput
    public void incrementMessageThroughput() {
        messageThroughput.increment();
    }

    // Broadcast deliveries
    public void incrementBroadcastDeliveries(int count) {
        broadcastDeliveries.increment(count);
    }

    // Delivery latency
    public void recordDeliveryLatency(double seconds) {
        deliveryLatency.record(seconds);
    }
}
