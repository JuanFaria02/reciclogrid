package com.collector.monitoring.reciclogrid.service;

import com.collector.monitoring.reciclogrid.domain.Employee;
import com.collector.monitoring.reciclogrid.domain.dto.EmployeeDTO;
import com.collector.monitoring.reciclogrid.domain.enums.UserType;
import com.collector.monitoring.reciclogrid.repository.EmployeeRepository;
import com.collector.monitoring.reciclogrid.service.exception.DatabaseException;
import com.collector.monitoring.reciclogrid.service.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @InjectMocks
    private EmployeeService employeeService;

    @Mock
    private EmployeeRepository employeeRepository;

    private Employee employee;

    @BeforeEach
    void setUp() {
        employee = new Employee();
        employee.setName("John Doe");
        employee.setEmail("john@example.com");
        employee.setPhone("123456789");
        employee.setType(UserType.ADMIN);
        employee.setDocumentNumber("12345678900");
        employee.setActive(true);
    }

    @Test
    void shouldFindByEmail() {
        when(employeeRepository.findByEmail("john@example.com")).thenReturn(employee);

        Employee found = employeeService.findByEmail("john@example.com");
        assertNotNull(found);
        assertEquals("john@example.com", found.getEmail());
    }

    @Test
    void shouldThrowExceptionWhenEmailNotFound() {
        when(employeeRepository.findByEmail("notfound@example.com")).thenReturn(null);
        assertThrows(ResourceNotFoundException.class, () -> employeeService.findByEmail("notfound@example.com"));
    }

    @Test
    void shouldInsertNewEmployee() {
        when(employeeRepository.save(employee)).thenReturn(employee);

        employeeService.insert(employee);
        verify(employeeRepository, times(1)).save(employee);
    }

    @Test
    void shouldFindById() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        EmployeeDTO dto = employeeService.findById(1L);
        assertNotNull(dto);
        assertEquals("John Doe", dto.name());
    }

    @Test
    void shouldThrowExceptionWhenEmployeeNotFoundById() {
        when(employeeRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> employeeService.findById(99L));
    }

    @Test
    void shouldInactivateEmployee() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        employeeService.inactivate(1L);
        assertFalse(employee.isActive());
        verify(employeeRepository, times(1)).save(employee);
    }

    @Test
    void shouldThrowExceptionWhenInactivatingSuperAdmin() {
        employee.setType(UserType.SUPERADMIN);
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        assertThrows(DatabaseException.class, () -> employeeService.inactivate(1L));
    }

    @Test
    void shouldUpdateEmployee() {
        EmployeeDTO dto = new EmployeeDTO("Updated Name", "updated@example.com", "987654321", UserType.ADMIN, "12345678900");
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        EmployeeDTO updatedDto = employeeService.update(dto, 1L);
        assertEquals("Updated Name", updatedDto.name());
    }

    @Test
    void shouldChangePassword() {
        String newPassword = "newPassword123";
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        EmployeeDTO updatedDto = employeeService.changePassword(newPassword, 1L);
        assertNotNull(updatedDto);
    }
}
