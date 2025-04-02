package com.collector.monitoring.reciclogrid.service;

import com.collector.monitoring.reciclogrid.domain.Collector;
import com.collector.monitoring.reciclogrid.domain.Microcontroller;
import com.collector.monitoring.reciclogrid.repository.MicrocontrollerRepository;
import com.collector.monitoring.reciclogrid.service.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MicrocontrollerService {
    @Autowired
    private MicrocontrollerRepository microcontrollerRepository;

    @Autowired
    private CollectorService collectorService;

    public Microcontroller findByIdentifierNumber(String identifierNumber) {
        final Microcontroller microcontroller = microcontrollerRepository.findByIdentifierNumber(identifierNumber);

        if (microcontroller == null) {
            throw new ResourceNotFoundException("O microcontrolador não foi encontrador");
        }
        return microcontroller;
    }

    public Microcontroller findByCollector(String collectorCode) {
        final Collector collector = collectorService.findByCode(collectorCode);
        return microcontrollerRepository.findByCollector(collector);
    }
}
