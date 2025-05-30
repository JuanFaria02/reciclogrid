package com.collector.monitoring.reciclogrid.repository;

import com.collector.monitoring.reciclogrid.domain.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
    Company findByDocumentNumber(String documentNumber);
}
