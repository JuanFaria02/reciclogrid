package com.collector.monitoring.reciclogrid.domain.dto;

import com.collector.monitoring.reciclogrid.domain.Address;
import com.collector.monitoring.reciclogrid.domain.Collector;
import com.collector.monitoring.reciclogrid.domain.Metric;
import com.collector.monitoring.reciclogrid.domain.Microcontroller;

import java.math.BigDecimal;
import java.util.Optional;

public record CollectorDTO(Long id, String name, String category, Address address, Integer distance, BigDecimal percentage, BigDecimal weight, boolean active, String code) {
    public static CollectorDTO buildCollectorDTO(Collector collector) {
        final Microcontroller microcontroller = collector.getMicrocontroller();

        final Metric lastMetric = Optional.ofNullable(microcontroller)
                .map(Microcontroller::getMetrics)
                .flatMap(metrics -> metrics.stream().findFirst())
                .orElse(null);

        final Integer distance = lastMetric != null ? lastMetric.getDistance() : 0;
        final BigDecimal percentage = lastMetric != null ? lastMetric.getPercentage() : BigDecimal.ZERO;
        final BigDecimal weight = lastMetric != null ? lastMetric.getWeight() : BigDecimal.ZERO;

        return new CollectorDTO(collector.getId(), collector.getName(), collector.getCategory(), collector.getAddress(), distance, percentage, weight, collector.isActive(), collector.getCode());
    }
}
