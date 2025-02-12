package com.collector.monitoring.reciclogrid.domain;

import jakarta.persistence.*;
import jdk.jfr.Timestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "collector")
public class Collector {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private boolean active = true;

    @Column(name = "created_at", nullable = false)
    @Timestamp
    private final LocalDateTime createdAt = LocalDateTime.now();

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", foreignKey = @ForeignKey(name = "address_fk_collector"))
    private Address address;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "employee_collectors",
            joinColumns = @JoinColumn(name = "collector_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "employee_id", nullable = false))
    private final Set<Employee> employees = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", foreignKey = @ForeignKey(name = "company_fk_collector"))
    private Company company;

    public Collector() {
    }

    public Collector(Long id, String name, Address address, String category, Company company) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.category = category;
        this.company = company;
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

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Set<Employee> getEmployees() {
        return employees;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }
}
