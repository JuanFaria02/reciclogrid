package com.collector.monitoring.reciclogrid.service;

import com.collector.monitoring.reciclogrid.domain.Company;
import com.collector.monitoring.reciclogrid.repository.CompanyRepository;
import com.collector.monitoring.reciclogrid.service.exception.DatabaseException;
import com.collector.monitoring.reciclogrid.service.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CompanyService {
    @Autowired
    private CompanyRepository companyRepository;

    @Transactional
    public void insert(Company company) {
        if (company.getId() != null) {
            throw new DatabaseException("Essa empresa já está cadastrada");
        }

        Company companyRegister = companyRepository.findByDocumentNumber(company.getDocumentNumber());

        if (companyRegister != null) {
            throw new DatabaseException("Essa empresa já está cadastrada");
        }

        companyRepository.save(company);
    }

    @Transactional
    public void update(Company obj) {
        try {
            final Company objCompany = companyRepository.findByDocumentNumber(obj.getDocumentNumber());

            Company company = Optional.ofNullable(objCompany).orElseThrow(() -> new ResourceNotFoundException(obj.getDocumentNumber()));

            updateOldCompany(obj, company);
            companyRepository.save(company);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(obj.getDocumentNumber());
        } catch (RuntimeException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    private void updateOldCompany(Company newObj, Company company) {
        company.setDocumentNumber(newObj.getDocumentNumber());
        company.setActive(newObj.isActive());
        company.setName(newObj.getName());
        company.setEmail(newObj.getEmail());
        company.setCorporateName(newObj.getCorporateName());
    }
}
