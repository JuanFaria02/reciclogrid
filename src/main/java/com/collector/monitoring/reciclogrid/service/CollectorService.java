package com.collector.monitoring.reciclogrid.service;

import com.collector.monitoring.reciclogrid.domain.Address;
import com.collector.monitoring.reciclogrid.domain.Collector;
import com.collector.monitoring.reciclogrid.domain.Employee;
import com.collector.monitoring.reciclogrid.domain.dto.CollectorDTO;
import com.collector.monitoring.reciclogrid.repository.CollectorRepository;
import com.collector.monitoring.reciclogrid.service.exception.AccessDeniedException;
import com.collector.monitoring.reciclogrid.service.exception.DatabaseException;
import com.collector.monitoring.reciclogrid.service.exception.ReciclogridException;
import com.collector.monitoring.reciclogrid.service.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CollectorService {
    @Autowired
    private CollectorRepository collectorRepository;
    @Autowired
    private AuthorizationService authorizationService;
    @Autowired
    private AddressService addressService;

    public List<CollectorDTO> findAll() {
        final Employee employee = (Employee) authorizationService.getUserLogged();
        return collectorRepository.findAll()
                .stream()
                .filter(collector -> authorizationService.userLoggedIsAdmin() || (collector.isActive() && collector.getEmployees().contains(employee)))
                .map(CollectorDTO::buildCollectorDTO)
                .toList();
    }

    public CollectorDTO findById(Long id) {
        final Collector collector = collectorRepository.findById(id).orElse(null);
        final Employee employee = (Employee) authorizationService.getUserLogged();

        if (collector == null) {
            throw new ResourceNotFoundException("Coletor não encontrado");
        }

        if (!collector.getEmployees().contains(employee)) {
            throw new AccessDeniedException("Usuário não possui acesso para este coletor");
        }

        return CollectorDTO.buildCollectorDTO(collector);
    }

    public Collector findByCode(String code) {
        try {
            return Optional.ofNullable(collectorRepository.findByCode(code))
                    .orElseThrow(() -> new ResourceNotFoundException("Coletor não existe."));
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
    }

    @Transactional
    public void insert(CollectorDTO collectorDTO) {
        if (!authorizationService.userLoggedIsAdmin()) {
            throw new AccessDeniedException("Acesso negado. Esse usuário não possui permissão para realizar essa ação");
        }

        try {
             Address existingAddress = addressService.findByCep(
                    collectorDTO.address().getCep());

            if (existingAddress == null) {
                final Address newAddress = collectorDTO.address();
                existingAddress = addressService.saveAddress(newAddress);
            }

            final Collector collector = new Collector(
                    collectorDTO.id(),
                    collectorDTO.name(),
                    existingAddress,
                    collectorDTO.category(),
                    null,
                    collectorDTO.code());

            collectorRepository.save(collector);
        } catch (Exception e) {
            throw new ReciclogridException(e.getMessage());
        }
    }

    @Transactional
    public CollectorDTO update(CollectorDTO obj, Long id) {
        try {
            final Optional<Collector> objCollector = collectorRepository.findById(id);

            Collector collector = objCollector.orElseThrow(() -> new ResourceNotFoundException(id));

            collector.copyDto(obj);
            collector = collectorRepository.save(collector);

            return CollectorDTO.buildCollectorDTO(collector);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (RuntimeException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    @Transactional
    public void changeStatus(Long id) {
        try {
            if (!authorizationService.userLoggedIsAdmin()) {
                throw new AccessDeniedException("Acesso negado. Esse usuário não possui permissão para realizar essa ação");
            }

            final Optional<Collector> objCollector = collectorRepository.findById(id);

            final Collector collector = objCollector.orElseThrow(() -> new ResourceNotFoundException(id));

            collector.changeStatus();

            collectorRepository.save(collector);
        }
        catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException(id);
        }
        catch (DataIntegrityViolationException e) {
            throw new DatabaseException(e.getMessage());
        } catch (AccessDeniedException e) {
            throw new AccessDeniedException(e.getMessage());
        }
    }
}
