package com.collector.monitoring.reciclogrid.domain;

import com.collector.monitoring.reciclogrid.domain.dto.EmployeeDTO;
import com.collector.monitoring.reciclogrid.domain.enums.UserType;
import jakarta.persistence.*;
import jdk.jfr.Timestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "employee")
public class Employee implements UserDetails, Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false, name = "type")
    @Enumerated(EnumType.STRING)
    private UserType type;

    @Column(name = "document_number", unique = true)
    private String documentNumber;

    @Column(nullable = false, columnDefinition = "text")
    private String password;

    @Column(nullable = false)
    private boolean active = true;

    @Column(name = "created_at", nullable = false)
    @Timestamp
    private final LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", foreignKey = @ForeignKey(name = "company_fk_employee"))
    private Company company;

    @ManyToMany(mappedBy = "employees")
    private final Set<Collector> collectors = new HashSet<>();

    public Employee() {
    }

    public Employee(Long id, String name, String email, String phone, Company company,
                    UserType type, String documentNumber, boolean active, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.company = company;
        this.type = type;
        this.documentNumber = documentNumber;
        this.active = active;
        this.password = password;
    }

    public void copyDto(EmployeeDTO employeeDTO) {

    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Set<Collector> getCollectors() {
        return collectors;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.type == UserType.ADMIN) {
            return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_USER"));
        }

        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return active;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return active;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    public UserType getType() {
        return type;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
