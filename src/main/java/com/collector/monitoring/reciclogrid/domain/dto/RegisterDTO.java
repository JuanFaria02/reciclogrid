package com.collector.monitoring.reciclogrid.domain.dto;

import com.collector.monitoring.reciclogrid.domain.Company;
import com.collector.monitoring.reciclogrid.domain.enums.UserType;

public record RegisterDTO(String name, String email, String password, String phone, Company company, UserType type, String documentNumber) {
}
