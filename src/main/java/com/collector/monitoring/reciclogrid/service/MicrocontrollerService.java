package com.collector.monitoring.reciclogrid.service;

import com.collector.monitoring.reciclogrid.domain.Collector;
import com.collector.monitoring.reciclogrid.domain.Microcontroller;
import com.collector.monitoring.reciclogrid.repository.MicrocontrollerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MicrocontrollerService {
    @Autowired
    private MicrocontrollerRepository microcontrollerRepository;

    @Autowired
    private CollectorService collectorService;

    public Microcontroller findByCollector(String collectorCode) {
        final Collector collector = collectorService.findByCode(collectorCode);
        return microcontrollerRepository.findByCollector(collector);
    }
}
