package com.collector.monitoring.reciclogrid.service;

import com.collector.monitoring.reciclogrid.domain.Collector;
import com.collector.monitoring.reciclogrid.domain.Sensor;
import com.collector.monitoring.reciclogrid.domain.dto.CollectorDTO;
import com.collector.monitoring.reciclogrid.repository.CollectorRepository;
import com.collector.monitoring.reciclogrid.repository.SensorRepository;
import com.collector.monitoring.reciclogrid.service.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CollectorService {
    @Autowired
    private CollectorRepository collectorRepository;
    @Autowired
    private SensorRepository sensorRepository;

    public List<CollectorDTO> findAll() {
        return collectorRepository.findAll()
                .stream()
                .filter(Collector::isActive)
                .map(collector -> {
                    final Sensor sensor = sensorRepository.findByCollector(collector);
                    if (sensor == null) {
                        throw new ResourceNotFoundException("Sensor não encontrado");
                    }

                    return CollectorDTO.buildCollectorDTO(sensor, collector);
                })
                .toList();
    }
}
