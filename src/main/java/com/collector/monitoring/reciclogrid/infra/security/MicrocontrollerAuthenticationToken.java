package com.collector.monitoring.reciclogrid.infra.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class MicrocontrollerAuthenticationToken extends AbstractAuthenticationToken {
    private final Object principal;
    private final String credentials;

    public MicrocontrollerAuthenticationToken(Object principal, String credentials) {
        super(null);
        this.principal = principal;
        this.credentials = credentials;
        setAuthenticated(false);
    }

    public MicrocontrollerAuthenticationToken(Object principal, String credentials, boolean authenticated) {
        super(null);
        this.principal = principal;
        this.credentials = credentials;
        setAuthenticated(authenticated);
    }

    @Override
    public Object getCredentials() {
        return credentials;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

}