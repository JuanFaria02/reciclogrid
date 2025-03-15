package com.collector.monitoring.reciclogrid.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "address",
        uniqueConstraints = { @UniqueConstraint(columnNames = { "latitude", "longitude" }) })
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String street;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String state;

    @Column(nullable = false, length = 2)
    private String uf;

    @Column(length = 128)
    private String latitude;

    @Column(length = 128)
    private String longitude;

    @Column(length = 50)
    private String cep;

    public Address() {}

    public Address(Long id, String street, String city, String uf, String state, String latitude, String longitude, String cep) {
        this.id = id;
        this.street = street;
        this.city = city;
        this.uf = uf;
        this.state = state;
        this.latitude = latitude;
        this.longitude = longitude;
        this.cep = cep;
    }

    public Long getId() {
        return id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }
}
