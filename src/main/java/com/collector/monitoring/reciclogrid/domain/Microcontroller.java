package com.collector.monitoring.reciclogrid.domain;

import jakarta.persistence.*;
import jdk.jfr.Timestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "microcontroller")
public class Microcontroller {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "identifier_number", nullable = false, unique = true)
    private String identifierNumber;

    @Column(nullable = false)
    private boolean active = true;

    @Column(name = "created_at", nullable = false)
    @Timestamp
    private final LocalDateTime createdAt = LocalDateTime.now();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "collector_id", foreignKey = @ForeignKey(name = "collector_fk_microcontroller"))
    private Collector collector;

    @OneToMany(mappedBy = "microcontroller", cascade = CascadeType.ALL)
    private final List<Metric> metrics = new ArrayList<>();

    public Microcontroller() {
    }

    public Microcontroller(Long id, String name, String identifierNumber, Collector collector) {
        this.id = id;
        this.name = name;
        this.identifierNumber = identifierNumber;
        this.collector = collector;
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

    public String getIdentifierNumber() {
        return identifierNumber;
    }

    public void setIdentifierNumber(String identifierNumber) {
        this.identifierNumber = identifierNumber;
    }

    public Collector getCollector() {
        return collector;
    }

    public void setCollector(Collector collector) {
        this.collector = collector;
    }

    public List<Metric> getMetrics() {
        return metrics;
    }

    public void setId(Long id) {
        this.id = id;
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
}
