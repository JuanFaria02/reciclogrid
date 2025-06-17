package com.collector.monitoring.reciclogrid.domain.dto;

import com.collector.monitoring.reciclogrid.domain.*;

import java.math.BigDecimal;
import java.util.Optional;

public record CollectorDTO(Long id, String name, String category, Address address, BigDecimal percentage, BigDecimal weight, boolean active, String code, CompanyDTO company) {
    public static CollectorDTO buildCollectorDTO(Collector collector) {
        final Microcontroller microcontroller = collector.getMicrocontroller();

        final Metric lastMetric = Optional.ofNullable(microcontroller)
                .map(Microcontroller::getMetrics)
                .flatMap(metrics -> metrics.stream().findFirst())
                .orElse(null);

        final BigDecimal percentage = lastMetric != null ? lastMetric.getPercentage() : BigDecimal.ZERO;
        final BigDecimal weight = lastMetric != null ? lastMetric.getWeight() : BigDecimal.ZERO;
        final CompanyDTO companyDTO = collector.getCompany() != null ?
                new CompanyDTO(collector.getCompany().getName(), collector.getCompany().getCorporateName(), collector.getCompany().getEmail(), collector.getCompany().getDocumentNumber()) : null;

        return new CollectorDTO(collector.getId(), collector.getName(), collector.getCategory(), collector.getAddress(), percentage, weight, collector.isActive(), collector.getCode(), companyDTO);
    }
}
