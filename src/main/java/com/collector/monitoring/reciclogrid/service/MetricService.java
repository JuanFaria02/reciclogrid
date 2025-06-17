package com.collector.monitoring.reciclogrid.service;

import com.collector.monitoring.reciclogrid.domain.Metric;
import com.collector.monitoring.reciclogrid.domain.Microcontroller;
import com.collector.monitoring.reciclogrid.domain.dto.MetricDTO;
import com.collector.monitoring.reciclogrid.repository.MetricRepository;
import com.collector.monitoring.reciclogrid.service.exception.ReciclogridException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class MetricService {
    @Autowired
    private MetricRepository metricRepository;
    @Autowired
    private MicrocontrollerService microcontrollerService;

    @Transactional
    public void insert(MetricDTO metricDTO) {
        valiateMetrics(metricDTO);
        final Microcontroller microcontroller = microcontrollerService.findByCollector(metricDTO.collectorCode());

        Metric metric = new Metric(null,
                metricDTO.percentage(),
                metricDTO.weight(),
                microcontroller);

        metricRepository.save(metric);
    }

    private void valiateMetrics(MetricDTO metricDTO) {
        if (metricDTO.collectorCode() == null) {
            throw new ReciclogridException("Identificador do microcontrolador não pode ser nulo");
        }

        if (metricDTO.percentage().compareTo(BigDecimal.ZERO) < 0 || metricDTO.percentage().compareTo(BigDecimal.valueOf(100)) > 0) {
            throw new ReciclogridException("Porcentagem registrada possui inconsistência. Porcentagem passada foi igual a " + metricDTO.percentage());
        }

        if (metricDTO.weight().compareTo(BigDecimal.ZERO) < 0) {
            throw new ReciclogridException("O peso registrado possui inconsistência. Peso enviado foi igual a " + metricDTO.percentage());
        }
    }
}
