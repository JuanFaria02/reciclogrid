package com.collector.monitoring.reciclogrid.repository;

import com.collector.monitoring.reciclogrid.domain.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Employee findByEmail(String email);
    Employee findByEmailOrDocumentNumber(String email, String documentNumber);
}
