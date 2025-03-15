package com.collector.monitoring.reciclogrid.service;

import com.collector.monitoring.reciclogrid.domain.Address;
import com.collector.monitoring.reciclogrid.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressService {
    @Autowired
    private AddressRepository addressRepository;

    public Address findByCep(String cep) {
        return addressRepository.findByCep(cep);
    }

    public Address saveAddress(Address address) {
        return addressRepository.save(address);
    }
}
