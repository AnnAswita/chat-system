package com.chatapp.chatsystemlayered.infrastructure.monitoring;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

//@Component
//public class MetricsCollector {
//
//    private final ConcurrentHashMap<String, AtomicLong> counters = new ConcurrentHashMap<>();
//
//    public void recordLatency(String metricName, long millis) {
//        counters.computeIfAbsent(metricName, k -> new AtomicLong()).addAndGet(millis);
//    }
//
//    public long getTotal(String metricName) {
//        return counters.getOrDefault(metricName, new AtomicLong(0)).get();
//    }
//}
