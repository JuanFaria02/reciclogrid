package com.collector.monitoring.reciclogrid.repository;

import com.collector.monitoring.reciclogrid.domain.Collector;
import com.collector.monitoring.reciclogrid.domain.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SensorRepository extends JpaRepository<Sensor, Long> {
    Sensor findByIdentifierNumber(String identifierNumber);
}
