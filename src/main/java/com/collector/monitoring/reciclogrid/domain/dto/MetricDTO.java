package com.collector.monitoring.reciclogrid.domain.dto;

import java.math.BigDecimal;

public record MetricDTO(String collectorCode, Integer distance, BigDecimal percentage, BigDecimal weight) {
}
