package com.collector.monitoring.reciclogrid.service;

import com.collector.monitoring.reciclogrid.domain.Metric;
import com.collector.monitoring.reciclogrid.domain.Microcontroller;
import com.collector.monitoring.reciclogrid.domain.dto.MetricDTO;
import com.collector.monitoring.reciclogrid.repository.MetricRepository;
import com.collector.monitoring.reciclogrid.service.exception.ReciclogridException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MetricServiceTest {

    @InjectMocks
    private MetricService metricService;

    @Mock
    private MetricRepository metricRepository;

    @Mock
    private MicrocontrollerService microcontrollerService;

    private Microcontroller microcontroller;

    @BeforeEach
    void setUp() {
        microcontroller = new Microcontroller();
    }

    @Test
    void shouldInsertMetricSuccessfully() {
        MetricDTO dto = new MetricDTO("COL001", 25, BigDecimal.valueOf(50), BigDecimal.valueOf(10));
        when(microcontrollerService.findByCollector("COL001")).thenReturn(microcontroller);

        metricService.insert(dto);

        verify(metricRepository, times(1)).save(any(Metric.class));
    }

    @Test
    void shouldThrowExceptionWhenCollectorCodeIsNull() {
        MetricDTO dto = new MetricDTO(null, 10, BigDecimal.valueOf(20), BigDecimal.ONE);

        ReciclogridException ex = assertThrows(ReciclogridException.class, () -> metricService.insert(dto));
        assertTrue(ex.getMessage().contains("Identificador do microcontrolador não pode ser nulo"));
    }

    @Test
    void shouldThrowExceptionWhenDistanceOrPercentageIsNull() {
        MetricDTO dto1 = new MetricDTO("COL002", null, BigDecimal.valueOf(10), BigDecimal.ONE);
        MetricDTO dto2 = new MetricDTO("COL002", 10, null, BigDecimal.ONE);

        assertThrows(ReciclogridException.class, () -> metricService.insert(dto1));
        assertThrows(ReciclogridException.class, () -> metricService.insert(dto2));
    }

    @Test
    void shouldThrowExceptionWhenDistanceIsNegative() {
        MetricDTO dto = new MetricDTO("COL003", -5, BigDecimal.valueOf(10), BigDecimal.ONE);

        ReciclogridException ex = assertThrows(ReciclogridException.class, () -> metricService.insert(dto));
        assertTrue(ex.getMessage().contains("Distância registradas no microcontrolador possui inconsistência"));
    }

    @Test
    void shouldThrowExceptionWhenPercentageIsInvalid() {
        MetricDTO dto1 = new MetricDTO("COL004", 5, BigDecimal.valueOf(-1), BigDecimal.ONE);
        MetricDTO dto2 = new MetricDTO("COL004", 5, BigDecimal.valueOf(150), BigDecimal.ONE);

        assertThrows(ReciclogridException.class, () -> metricService.insert(dto1));
        assertThrows(ReciclogridException.class, () -> metricService.insert(dto2));
    }

    @Test
    void shouldThrowExceptionWhenWeightIsNegative() {
        MetricDTO dto = new MetricDTO("COL005", 5, BigDecimal.valueOf(20), BigDecimal.valueOf(-1));

        ReciclogridException ex = assertThrows(ReciclogridException.class, () -> metricService.insert(dto));
        assertTrue(ex.getMessage().contains("Peso enviado foi igual a"));
    }
}
