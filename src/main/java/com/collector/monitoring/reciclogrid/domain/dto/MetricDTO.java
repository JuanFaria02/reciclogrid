package com.collector.monitoring.reciclogrid.domain.dto;

import java.math.BigDecimal;

public record MetricDTO(String identifierNumberSensor, Integer distance, BigDecimal percentage) {
}
