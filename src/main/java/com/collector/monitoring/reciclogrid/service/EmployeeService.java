package com.collector.monitoring.reciclogrid.service;

import com.collector.monitoring.reciclogrid.domain.Employee;
import com.collector.monitoring.reciclogrid.domain.dto.EmployeeDTO;
import com.collector.monitoring.reciclogrid.domain.enums.UserType;
import com.collector.monitoring.reciclogrid.repository.EmployeeRepository;
import com.collector.monitoring.reciclogrid.service.exception.DatabaseException;
import com.collector.monitoring.reciclogrid.service.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;

    public Employee findByEmail(String email) {
        try {
            final Employee employee = employeeRepository.findByEmail(email);

            if (employee == null) {
                throw new ResourceNotFoundException(email);
            }

            return employee;
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("User with email " + email + " not found");
        }
    }

    @Transactional
    public void insert(Employee employee) {
        if (employee.getId() != null) {
            throw new DatabaseException("Esse usuário já está cadastrado");
        }

        Employee employeeInactive = employeeRepository.findByEmailOrDocumentNumber(employee.getDocumentNumber(), employee.getEmail());

        if (employeeInactive != null) {
            employee.setActive(true);

            if (!employee.isActive()) {
                throw new DatabaseException("Esse usuário já está cadastrado");
            }

            employeeRepository.save(employeeInactive);
            return;
        }

        employeeRepository.save(employee);
    }

    public Employee findByDocumentNumber(String documentNumber) {
        try {
            return employeeRepository.findByDocumentNumber(documentNumber);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("User with email " + documentNumber + " not found");
        }
    }

    public List<EmployeeDTO> findAll() {
        return employeeRepository.findAll()
                .stream()
                .filter(Employee::isActive)
                .map(employee -> new EmployeeDTO(employee.getId(), employee.getName(), employee.getEmail(), employee.getPhone(), employee.getType(), employee.getDocumentNumber()))
                .toList();
    }

    public EmployeeDTO findById(Long id) {
        Optional<Employee> obj = employeeRepository.findById(id);

        final Employee employee = obj.orElseThrow(()-> new ResourceNotFoundException(id));
        return new EmployeeDTO(employee.getId(), employee.getName(), employee.getEmail(), employee.getPhone(), employee.getType(), employee.getDocumentNumber());
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

            employee.copyDto(obj);
            employee = employeeRepository.save(employee);
            return new EmployeeDTO(employee.getId(), employee.getName(), employee.getEmail(), employee.getPhone(), employee.getType(), employee.getDocumentNumber());
        } catch (RuntimeException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

//    TODO Melhorar alteração de senha para que não seja feito apenas pelo admin
    public EmployeeDTO changePassword(String newPassword, Long id) {
        try {
            final Optional<Employee> objEmployee = employeeRepository.findById(id);

            Employee employee = objEmployee.orElseThrow(() -> new ResourceNotFoundException(id));

            if (newPassword != null && !newPassword.isBlank()) {
                String encryptedPassword = new BCryptPasswordEncoder().encode(newPassword);
                employee.setPassword(encryptedPassword);
            }

            employee = employeeRepository.save(employee);
            return new EmployeeDTO(employee.getId(), employee.getName(), employee.getEmail(), employee.getPhone(), employee.getType(), employee.getDocumentNumber());
        } catch (RuntimeException e) {
            throw new DatabaseException(e.getMessage());
        }
    }
}
