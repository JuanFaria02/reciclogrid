package com.collector.monitoring.reciclogrid.repository;

import com.collector.monitoring.reciclogrid.domain.Collector;
import com.collector.monitoring.reciclogrid.domain.Microcontroller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MicrocontrollerRepository extends JpaRepository<Microcontroller, Long> {
    Microcontroller findByCollector(Collector collector);
    Microcontroller findByName(String name);
}
