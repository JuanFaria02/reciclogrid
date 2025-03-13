package com.collector.monitoring.reciclogrid.controller;

import com.collector.monitoring.reciclogrid.domain.dto.CollectorDTO;
import com.collector.monitoring.reciclogrid.service.CollectorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.collector.monitoring.reciclogrid.utils.Constants.API_PATH;

@RestController
@RequestMapping(API_PATH)
public class CollectorController {

    @Autowired
    private CollectorService collectorService;

    @GetMapping(value = "/collectors")
    public ResponseEntity<List<CollectorDTO>> findAll() {
        final List<CollectorDTO> collectorDTOS = collectorService.findAll();
        return ResponseEntity.ok()
                .body(collectorDTOS);
    }

    @PutMapping("/collector/{id}")
    public ResponseEntity<CollectorDTO> update(@RequestBody CollectorDTO obj, @PathVariable Long id) {
        final CollectorDTO collectorDTO = collectorService.update(obj, id);
        return ResponseEntity.ok().body(collectorDTO);
    }

}
