package com.collector.monitoring.reciclogrid.service;

import com.collector.monitoring.reciclogrid.domain.Company;
import com.collector.monitoring.reciclogrid.domain.Employee;
import com.collector.monitoring.reciclogrid.domain.dto.CompanyDTO;
import com.collector.monitoring.reciclogrid.domain.dto.EmployeeDTO;
import com.collector.monitoring.reciclogrid.domain.enums.UserType;
import com.collector.monitoring.reciclogrid.repository.EmployeeRepository;
import com.collector.monitoring.reciclogrid.service.exception.DatabaseException;
import com.collector.monitoring.reciclogrid.service.exception.ReciclogridException;
import com.collector.monitoring.reciclogrid.service.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private CollectorService collectorService;
    @Autowired
    private AuthorizationService authorizationService;

    public Employee findByEmail(String email) {
        try {
            return employeeRepository.findByEmail(email);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Usuário com o email " + email + " não encontrado");
        }
    }

    @Transactional
    public void insert(Employee employee) {
        if (employee.getId() != null) {
            throw new DatabaseException("Esse usuário já está cadastrado");
        }

        Employee employeeInactive = employeeRepository.findByEmailOrDocumentNumber(employee.getDocumentNumber(), employee.getEmail());

        if (employeeInactive != null) {
            if (!employee.isActive()) {
                throw new DatabaseException("Esse usuário já está cadastrado");
            }
            employee.setActive(true);

            employeeRepository.save(employeeInactive);
            return;
        }

        final boolean employeeHasDocumentNumber = employee.getDocumentNumber() != null || !employee.getDocumentNumber().isBlank();
        if (employee.getType() == UserType.EMPLOYEE && !employeeHasDocumentNumber) {
            throw new ReciclogridException("O número do documento de um funcionário não pode ser nulo.");
        }

        if (employee.getCompany() == null) {
            throw new ReciclogridException("A empresa do usuário não pode ser nula.");
        }

        final Company company = companyService.findByDocumentNumber(employee.getCompany().getDocumentNumber());
        employee.setCompany(company);

        employeeRepository.save(employee);
    }

    public Page<EmployeeDTO> findAll(Pageable pageable) {
        Employee employeeLogged = (Employee) authorizationService.getUserLogged();
        Page<Employee> employeePage = employeeRepository.findAll(pageable);

        List<EmployeeDTO> dtoList = employeePage.getContent()
                .stream()
                .filter(Employee::isActive)
                .filter(employee -> employeeLogged.getType() == UserType.ADMIN ? employee.getCompany().getDocumentNumber().equals(employeeLogged.getCompany().getDocumentNumber()) : true)
                .map(employee -> new EmployeeDTO(
                        employee.getId(),
                        employee.getName(),
                        employee.getEmail(),
                        employee.getPhone(),
                        employee.getType(),
                        getQtdCollectors(employee),
                        employee.getDocumentNumber(),
                        employee.getPosition(),
                        employee.getCompany() != null ? new CompanyDTO(employee.getCompany().getName(), employee.getCompany().getCorporateName(), employee.getCompany().getEmail(), employee.getCompany().getDocumentNumber()) : null))
                .toList();

        return new PageImpl<>(dtoList, pageable, dtoList.size());
    }

    public EmployeeDTO findById(Long id) {
        Optional<Employee> obj = employeeRepository.findById(id);

        final Employee employee = obj.orElseThrow(()-> new ResourceNotFoundException(id));
        return getEmployeeDTO(employee);
    }

    @Transactional
    public void inactivate(Long id) {
        try {
            final Optional<Employee> objEmployee = employeeRepository.findById(id);

            final Employee employee = objEmployee.orElseThrow(() -> new ResourceNotFoundException(id));

            if (employee.getType() == UserType.SUPERADMIN) {
                throw new DataIntegrityViolationException("Não é possível deletar o superadmin");
            }
            employee.setActive(false);

            employeeRepository.save(employee);
        }
        catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException(id);
        }
        catch (DataIntegrityViolationException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    @Transactional
    public EmployeeDTO update(EmployeeDTO obj, Long id) {
        try {
            final Optional<Employee> objEmployee = employeeRepository.findById(id);

            Employee employee = objEmployee.orElseThrow(() -> new ResourceNotFoundException(id));

            if (obj.company() != null) {
                Company company = companyService.findByDocumentNumber(obj.company().documentNumber());
                employee.setCompany(company);
            }
            employee.copyDto(obj);
            employee = employeeRepository.save(employee);
            return getEmployeeDTO(employee);
        } catch (RuntimeException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    private EmployeeDTO getEmployeeDTO(Employee employee) {
        final CompanyDTO companyDTO = employee.getCompany() != null ? new CompanyDTO(employee.getCompany().getName(), employee.getCompany().getCorporateName(), employee.getCompany().getEmail(), employee.getCompany().getDocumentNumber()) : null;
        final Integer qtdCollectors = getQtdCollectors(employee);

        return new EmployeeDTO(employee.getId(), employee.getName(), employee.getEmail(), employee.getPhone(), employee.getType(), qtdCollectors, employee.getDocumentNumber(), employee.getPosition(), companyDTO);
    }

    private Integer getQtdCollectors(Employee employee) {
        if (employee.getType() == UserType.SUPERADMIN) {
            return collectorService.findAll().size();
        }
        return employee.getCompany() != null ? employee.getCompany().getCollectors().size() : 0;
    }

    public EmployeeDTO changePassword(String newPassword, Long id) {
        try {
            final Optional<Employee> objEmployee = employeeRepository.findById(id);

            Employee employee = objEmployee.orElseThrow(() -> new ResourceNotFoundException(id));

            if (newPassword != null && !newPassword.isBlank()) {
                String encryptedPassword = new BCryptPasswordEncoder().encode(newPassword);
                employee.setPassword(encryptedPassword);
            }

            employee = employeeRepository.save(employee);
            return new EmployeeDTO(employee.getId(), employee.getName(), employee.getEmail(), employee.getPhone(), employee.getType(), null, employee.getDocumentNumber(), null, null);
        } catch (RuntimeException e) {
            throw new DatabaseException(e.getMessage());
        }
    }
}
