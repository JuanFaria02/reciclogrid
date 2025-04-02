package com.collector.monitoring.reciclogrid.infra.security;

import com.collector.monitoring.reciclogrid.domain.Microcontroller;
import com.collector.monitoring.reciclogrid.repository.MicrocontrollerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class MicrocontrollerAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    private MicrocontrollerRepository microcontrollerRepository;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String identifierNumber = authentication.getName();
        String name = authentication.getCredentials().toString();

        Microcontroller microcontroller = Optional.ofNullable(microcontrollerRepository.findByIdentifierNumber(identifierNumber))
                .orElseThrow(() -> new BadCredentialsException("Microcontrolador não encontrado"));

        if (!microcontroller.getName().equals(name)) {
            throw new BadCredentialsException("Nome do Microcontrolador inválido");
        }
        return new MicrocontrollerAuthenticationToken(microcontroller, identifierNumber, null);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return MicrocontrollerAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
