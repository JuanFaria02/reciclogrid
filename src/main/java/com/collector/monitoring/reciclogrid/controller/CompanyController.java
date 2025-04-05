package com.collector.monitoring.reciclogrid.controller;

import com.collector.monitoring.reciclogrid.domain.Company;
import com.collector.monitoring.reciclogrid.domain.dto.CollectorDTO;
import com.collector.monitoring.reciclogrid.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.collector.monitoring.reciclogrid.utils.Constants.API_PATH;

@RestController
@RequestMapping(API_PATH)
public class CompanyController {
    @Autowired
    private CompanyService companyService;

    @PutMapping("/company/update")
    public ResponseEntity<CollectorDTO> update(@RequestBody Company obj) {
        companyService.update(obj);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/company/create")
    public ResponseEntity<CollectorDTO> create(@RequestBody Company obj) {
        companyService.insert(obj);
        return ResponseEntity.ok().build();
    }

}
