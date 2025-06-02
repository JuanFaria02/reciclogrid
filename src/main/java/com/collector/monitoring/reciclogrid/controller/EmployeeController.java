package com.collector.monitoring.reciclogrid.controller;

import com.collector.monitoring.reciclogrid.domain.dto.EmployeeDTO;
import com.collector.monitoring.reciclogrid.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.collector.monitoring.reciclogrid.utils.Constants.API_PATH;

@RestController
@RequestMapping(API_PATH)
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/employees")
    public ResponseEntity<Page<EmployeeDTO>> findAll(
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

        Page<EmployeeDTO> resultPage = employeeService.findAll(pageable);

        return ResponseEntity.ok(resultPage);
    }

    @GetMapping(value = "/employee/{id}")
    public ResponseEntity<EmployeeDTO> findById(@PathVariable Long id) {
        final EmployeeDTO employee = employeeService.findById(id);
        return ResponseEntity.ok().body(employee);
    }

    @DeleteMapping("/employee/{id}")
    public ResponseEntity<Void> inactivate(@PathVariable Long id) {
        employeeService.inactivate(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/employee/{id}")
    public ResponseEntity<EmployeeDTO> update(@RequestBody EmployeeDTO obj, @PathVariable Long id) {
        final EmployeeDTO employeeDTO = employeeService.update(obj, id);
        return ResponseEntity.ok().body(employeeDTO);
    }

    @PutMapping("change-password/employee/{id}")
    public ResponseEntity<EmployeeDTO> changePassword(@RequestBody Map<String, String> request, @PathVariable Long id) {
        final String password = request.get("password");
        final EmployeeDTO employeeDTO = employeeService.changePassword(password, id);
        return ResponseEntity.ok().body(employeeDTO);
    }
}
