package com.collector.monitoring.reciclogrid.infra.security;

import com.collector.monitoring.reciclogrid.domain.Sensor;
import com.collector.monitoring.reciclogrid.repository.SensorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SensorAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    private SensorRepository sensorRepository;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String identifierNumber = authentication.getName();
        String name = authentication.getCredentials().toString();

        Sensor sensor = Optional.ofNullable(sensorRepository.findByIdentifierNumber(identifierNumber))
                .orElseThrow(() -> new BadCredentialsException("Sensor não encontrado"));

        if (!sensor.getName().equals(name)) {
            throw new BadCredentialsException("Nome do sensor inválido");
        }
        return new SensorAuthenticationToken(sensor, identifierNumber, null);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return SensorAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
