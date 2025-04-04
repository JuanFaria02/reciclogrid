package com.collector.monitoring.reciclogrid.domain;

import jakarta.persistence.*;
import jdk.jfr.Timestamp;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "metric")
public class Metric implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer distance;

    @Column(nullable = false)
    private BigDecimal percentage;

    @Column(nullable = false)
    private BigDecimal weight;

    @Column(name = "created_at", nullable = false)
    @Timestamp
    private final LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "microcontroller_id", foreignKey = @ForeignKey(name = "microcontroller_fk_metric"))
    private Microcontroller microcontroller;

    public Metric() {
    }

    public Metric(Long id, Integer distance, BigDecimal percentage, BigDecimal weight, Microcontroller microcontroller) {
        this.id = id;
        this.distance = distance;
        this.percentage = percentage;
        this.weight = weight;
        this.microcontroller = microcontroller;
    }

    public Long getId() {
        return id;
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    public BigDecimal getPercentage() {
        return percentage;
    }

    public void setPercentage(BigDecimal percentage) {
        this.percentage = percentage;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Microcontroller getMicrocontroller() {
        return microcontroller;
    }

    public void setMicrocontroller(Microcontroller microcontroller) {
        this.microcontroller = microcontroller;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }
}
