package com.collector.monitoring.reciclogrid.service;

import com.collector.monitoring.reciclogrid.domain.Collector;
import com.collector.monitoring.reciclogrid.domain.Sensor;
import com.collector.monitoring.reciclogrid.domain.dto.CollectorDTO;
import com.collector.monitoring.reciclogrid.domain.dto.EmployeeDTO;
import com.collector.monitoring.reciclogrid.repository.CollectorRepository;
import com.collector.monitoring.reciclogrid.repository.SensorRepository;
import com.collector.monitoring.reciclogrid.service.exception.DatabaseException;
import com.collector.monitoring.reciclogrid.service.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    @Transactional
    public CollectorDTO update(CollectorDTO obj, Long id) {
        try {
            final Optional<Collector> objCollector = collectorRepository.findById(id);

            Collector collector = objCollector.orElseThrow(() -> new ResourceNotFoundException(id));

            collector.copyDto(obj);
            collector = collectorRepository.save(collector);
            return CollectorDTO.buildCollectorDTO(sensorRepository.findByCollector(collector), collector);
        } catch (RuntimeException e) {
            throw new DatabaseException(e.getMessage());
        }
    }
}
