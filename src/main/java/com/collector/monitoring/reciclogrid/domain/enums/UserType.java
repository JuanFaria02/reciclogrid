package com.collector.monitoring.reciclogrid.domain.enums;

public enum UserType {
    EMPLOYEE("employee"),
    ADMIN("admin");

    private String role;

    UserType(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }

}
