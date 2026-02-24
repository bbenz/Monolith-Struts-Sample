package com.skishop.config;

import org.springframework.context.annotation.Configuration;

/**
 * Metrics Configuration for Prometheus
 * Note: Spring Boot 3.4.x auto-configures Prometheus metrics when 
 * micrometer-registry-prometheus is on the classpath.
 * No manual bean configuration needed.
 */
@Configuration
public class MetricsConfig {
    // Prometheus metrics are auto-configured by Spring Boot Actuator
    // Access metrics at: /actuator/prometheus
}
