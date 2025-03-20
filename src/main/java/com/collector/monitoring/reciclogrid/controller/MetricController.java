package com.collector.monitoring.reciclogrid.controller;

import com.collector.monitoring.reciclogrid.domain.dto.CollectorDTO;
import com.collector.monitoring.reciclogrid.domain.dto.MetricDTO;
import com.collector.monitoring.reciclogrid.service.MetricService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.collector.monitoring.reciclogrid.utils.Constants.API_PATH;

@RestController
@RequestMapping(API_PATH)
public class MetricController {
    @Autowired
    private MetricService metricService;

    @PostMapping("/metrics/insert")
    public ResponseEntity<CollectorDTO> insert(@RequestBody MetricDTO obj) {
        metricService.insert(obj);
        return ResponseEntity.ok().build();
    }

}
