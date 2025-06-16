package com.collector.monitoring.reciclogrid.domain.dto;

import com.collector.monitoring.reciclogrid.domain.enums.UserType;

public record EmployeeDTO(Long id, String name, String email, String phone, UserType type, Integer qtdCollectos, String documentNumber, String position, CompanyDTO company) {
}
