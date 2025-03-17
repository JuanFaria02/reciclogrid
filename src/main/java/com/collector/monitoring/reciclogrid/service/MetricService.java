package com.collector.monitoring.reciclogrid.service;

import com.collector.monitoring.reciclogrid.domain.Metric;
import com.collector.monitoring.reciclogrid.domain.Sensor;
import com.collector.monitoring.reciclogrid.domain.dto.MetricDTO;
import com.collector.monitoring.reciclogrid.repository.MetricRepository;
import com.collector.monitoring.reciclogrid.service.exception.ReciclogridException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import java.math.BigDecimal;

@Service
public class MetricService {
    @Autowired
    private MetricRepository metricRepository;
    @Autowired
    private SensorService sensorService;

    public void insert(MetricDTO metricDTO) {
        if (metricDTO.identifierNumberSensor() == null) {
            throw new ReciclogridException("Identificador do sensor não pode ser nulo");
        }

        if (metricDTO.distance() == null || metricDTO.percentage() == null) {
            throw new ReciclogridException("Distância ou porcentagem registradas no sensor não podem ser nulas");
        }

        if (metricDTO.distance() < 0) {
            throw new ReciclogridException("Distância registradas no sensor possui inconsistência");
        }

        if (metricDTO.percentage().compareTo(BigDecimal.ZERO) < 0 || metricDTO.percentage().compareTo(BigDecimal.valueOf(100)) > 0) {
            throw new ReciclogridException("Porcentagem registrada possui inconsistência. Porcentagem passada foi igual a " + metricDTO.percentage());
        }

        final Sensor sensor = sensorService.findByIdentifierNumber(metricDTO.identifierNumberSensor());
        Metric metric = new Metric(null, metricDTO.distance(), metricDTO.percentage(), sensor);

        metricRepository.save(metric);
    }
}
