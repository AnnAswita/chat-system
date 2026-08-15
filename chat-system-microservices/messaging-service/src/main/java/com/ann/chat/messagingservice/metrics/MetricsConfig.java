package com.ann.chat.messagingservice.metrics;
import io.micrometer.core.instrument.*;
import io.micrometer.core.instrument.binder.jvm.*;
import io.micrometer.core.instrument.binder.system.*;
import io.micrometer.core.instrument.binder.http.*;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.context.event.EventListener;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.concurrent.atomic.AtomicInteger;

@Configuration
public class MetricsConfig {

    private final MeterRegistry registry;
    private final SimpUserRegistry userRegistry;

    private Counter messageThroughput;
    private Timer restCallLatency;
    private Timer kafkaPublishLatency;
    private Timer deliveryLatency;
    private AtomicInteger activeSessions = new AtomicInteger(0);
    public MetricsConfig(MeterRegistry registry, SimpUserRegistry userRegistry) {
        this.registry = registry;
        this.userRegistry = userRegistry;
    }

    @PostConstruct
    public void init() {

        // Active WebSocket Sessions
        Gauge.builder("chat_websocket_sessions", activeSessions::get)
                .description("Active WebSocket sessions")
                .register(registry);

        // Message Throughput Counter
        messageThroughput = Counter.builder("chat_message_throughput_total")
                .description("Total messages sent through WebSocket")
                .register(registry);
        // Microservice communication latency (Messaging → Chat-Service)
        restCallLatency = Timer.builder("rest_call_latency_seconds")
                .publishPercentileHistogram()
                .description("Latency of REST call to chat-service")
                .register(registry);
        kafkaPublishLatency = Timer.builder("kafka_publish_latency_seconds")
                .publishPercentileHistogram()
                .description("Latency of publishing message to Kafka")
                .register(registry);
        // Delivery Latency Histogram
        deliveryLatency = Timer.builder("chat_deliverMessage_latency_seconds")
                .publishPercentileHistogram()
                .description("Latency of delivering WebSocket messages")
                .register(registry);

        // JVM + System Metrics
        new JvmMemoryMetrics().bindTo(registry);
        new JvmGcMetrics().bindTo(registry);
        new ProcessorMetrics().bindTo(registry);
        new ClassLoaderMetrics().bindTo(registry);
        new FileDescriptorMetrics().bindTo(registry);

    }
    @EventListener
    public void handleSessionConnected(SessionConnectEvent event) {
        activeSessions.incrementAndGet();
    }

    @EventListener
    public void handleSessionDisconnect(SessionDisconnectEvent event) {
        activeSessions.decrementAndGet();
    }

    public void incrementMessageThroughput() {
        messageThroughput.increment();
    }

    public void recordDeliveryLatency(Runnable deliveryLogic) {
        deliveryLatency.record(deliveryLogic);
    }
    // Microservice REST latency
    public void recordRestCallLatency(Runnable logic) {
        restCallLatency.record(logic);
    }

    // Kafka publish latency
    public void recordKafkaPublishLatency(Runnable logic) {
        kafkaPublishLatency.record(logic);
    }
}

