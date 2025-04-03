package com.collector.monitoring.reciclogrid.infra.security;

import com.collector.monitoring.reciclogrid.domain.Microcontroller;
import com.collector.monitoring.reciclogrid.repository.MicrocontrollerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class MicrocontrollerAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    private MicrocontrollerRepository microcontrollerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String name = authentication.getName();
        String identifierNumber = authentication.getCredentials().toString();

        Microcontroller microcontroller = Optional.ofNullable(microcontrollerRepository.findByName(name))
                .orElseThrow(() -> new BadCredentialsException("Microcontrolador não encontrado"));

        if (!microcontroller.getName().equals(name)) {
            throw new BadCredentialsException("Nome do Microcontrolador inválido");
        }

        if (!passwordEncoder.matches(identifierNumber, microcontroller.getIdentifierNumber())) {
            throw new BadCredentialsException("Identificador único inválido");
        }
        return new MicrocontrollerAuthenticationToken(microcontroller, identifierNumber, true);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return MicrocontrollerAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
