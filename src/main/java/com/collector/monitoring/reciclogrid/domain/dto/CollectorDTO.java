package com.collector.monitoring.reciclogrid.domain.dto;

import com.collector.monitoring.reciclogrid.domain.Address;
import com.collector.monitoring.reciclogrid.domain.Collector;
import com.collector.monitoring.reciclogrid.domain.Metric;
import com.collector.monitoring.reciclogrid.domain.Sensor;

import java.math.BigDecimal;

public record CollectorDTO(String name, String category, Address address, Integer distance, BigDecimal percentage, boolean active) {
    public static CollectorDTO buildCollectorDTO(Sensor sensor, Collector collector) {
        final Metric lastMetric = sensor.getMetrics()
                .stream()
                .findFirst()
                .orElse(null);

        final Integer distance = lastMetric != null ? lastMetric.getDistance() : 0;
        final BigDecimal percentage = lastMetric != null ? lastMetric.getPercentage() : BigDecimal.ZERO;

        return new CollectorDTO(collector.getName(), collector.getCategory(), collector.getAddress(), distance, percentage, collector.isActive());
    }
}
