package com.collector.monitoring.reciclogrid.domain.dto;

import java.math.BigDecimal;

public record MetricDTO(String collectorCode, BigDecimal percentage, BigDecimal weight) {
}
