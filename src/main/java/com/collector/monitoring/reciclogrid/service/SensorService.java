package com.collector.monitoring.reciclogrid.service;

import com.collector.monitoring.reciclogrid.domain.Sensor;
import com.collector.monitoring.reciclogrid.repository.SensorRepository;
import com.collector.monitoring.reciclogrid.service.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SensorService {
    @Autowired
    private SensorRepository sensorRepository;

    public Sensor findByIdentifierNumber(String identifierNumber) {
        final Sensor sensor = sensorRepository.findByIdentifierNumber(identifierNumber);

        if (sensor == null) {
            throw new ResourceNotFoundException("Sensor de identificador " + identifierNumber + " não encontrado");
        }
        return sensor;
    }
}
