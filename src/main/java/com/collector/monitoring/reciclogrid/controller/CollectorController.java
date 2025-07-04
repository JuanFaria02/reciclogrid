package com.collector.monitoring.reciclogrid.controller;

import com.collector.monitoring.reciclogrid.domain.dto.CollectorDTO;
import com.collector.monitoring.reciclogrid.service.CollectorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

import static com.collector.monitoring.reciclogrid.utils.Constants.API_PATH;

@RestController
@RequestMapping(API_PATH)
public class CollectorController {
    @Autowired
    private CollectorService collectorService;

    @GetMapping("/collectors")
    public ResponseEntity<Page<CollectorDTO>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        Sort.Order order = new Sort.Order(
                Sort.Direction.fromString(direction),
                sort
        );

        Pageable pageable = PageRequest.of(page, size, Sort.by(order));

        Page<CollectorDTO> resultPage = collectorService.findAll(pageable);

        return ResponseEntity.ok(resultPage);
    }

    @PutMapping("/collector/{id}")
    public ResponseEntity<CollectorDTO> update(@RequestBody CollectorDTO obj, @PathVariable Long id) {
        final CollectorDTO collectorDTO = collectorService.update(obj, id);
        return ResponseEntity.ok().body(collectorDTO);
    }

    @GetMapping("/collector/{id}")
    public ResponseEntity<CollectorDTO> findById(@PathVariable Long id) {
        final CollectorDTO collectorDTO = collectorService.findById(id);
        return ResponseEntity.ok().body(collectorDTO);
    }

    @PostMapping("/collector/create")
    public ResponseEntity<CollectorDTO> create(@RequestBody CollectorDTO obj) {
        collectorService.insert(obj);
        final URI uri = ServletUriComponentsBuilder.fromUriString("/api/collector/{id}")
                .buildAndExpand(obj.id()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PutMapping("/collector/change-status/{id}")
    public ResponseEntity<Void> changeStatus(@PathVariable Long id) {
        collectorService.changeStatus(id);
        return ResponseEntity.noContent().build();
    }

}
