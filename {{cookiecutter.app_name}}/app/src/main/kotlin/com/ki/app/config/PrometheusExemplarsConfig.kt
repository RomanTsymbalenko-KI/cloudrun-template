package com.ki.app.config

import io.micrometer.core.instrument.Clock
import io.micrometer.prometheus.PrometheusConfig
import io.micrometer.prometheus.PrometheusMeterRegistry
import io.prometheus.client.CollectorRegistry
import org.springframework.boot.actuate.autoconfigure.metrics.export.ConditionalOnEnabledMetricsExport
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * Prometheus config override
 * https://github.com/GoogleCloudPlatform/prometheus-engine/issues/812
 * https://github.com/micrometer-metrics/micrometer/pull/3996
 */
@Configuration
@ConditionalOnEnabledMetricsExport("prometheus")
@ConditionalOnProperty("prometheus.disable-exemplar-counter", matchIfMissing = true, havingValue = "true")
class PrometheusExemplarsConfig {
    @Bean
    @ConditionalOnMissingBean
    fun prometheusMeterRegistry(
        prometheusConfig: PrometheusConfig,
        collectorRegistry: CollectorRegistry,
        clock: Clock
    ): PrometheusMeterRegistry {
        return PrometheusMeterRegistry(prometheusConfig, collectorRegistry, clock)
    }
}
