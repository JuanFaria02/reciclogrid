package com.collector.monitoring.reciclogrid.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.*;
import com.collector.monitoring.reciclogrid.domain.Employee;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.collector.monitoring.reciclogrid.infra.security.exception.TokenException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class TokenService {
    @Value("${api.security.token.secret}")
    private String secret;

    public String generateAccessToken(Employee user) {
        Instant expirationDate = Instant.now().plus(5, ChronoUnit.MINUTES);
        return generateToken(user, expirationDate);
    }

    public String generateRefreshToken(Employee user) {
        Instant expirationDate = Instant.now().plus(10, ChronoUnit.DAYS);
        return generateToken(user, expirationDate);
    }

    public String generateMicrocontrollerToken(String identifierNumberMicrocontroller) {
        Instant expirationDate = Instant.now().plus(30, ChronoUnit.DAYS);
        return generateToken(identifierNumberMicrocontroller, expirationDate);
    }

    public void isTokenValid(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            DecodedJWT decodedJWT = JWT.decode(token);

            algorithm.verify(decodedJWT);

            String issuer = decodedJWT.getIssuer();
            if (!"auth-api".equals(issuer)) {
                throw new TokenException("Token com emissor inválido.");
            }

        } catch (JWTDecodeException e) {
            throw new TokenException("Token malformado.");
        } catch (SignatureVerificationException e) {
            throw new TokenException("Assinatura do token inválida.");
        } catch (RuntimeException e) {
            throw new TokenException("Token inválido: " + e.getMessage());
        }
    }

    private String generateToken(Employee user, Instant expirationDate) {
        try{
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer("auth-api")
                    .withSubject(user.getEmail())
                    .withClaim("type", user.getType().name())
                    .withExpiresAt(expirationDate)
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Error while generating token", exception);
        }
    }

    private String generateToken(String identifierNumberMicrocontroller, Instant expirationDate) {
        try{
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer("auth-api")
                    .withSubject(identifierNumberMicrocontroller)
                    .withExpiresAt(expirationDate)
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Error while generating token", exception);
        }
    }


    public String validateToken(String token){
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("auth-api")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (TokenExpiredException exception) {
            throw new TokenException("TOKEN EXPIRED");
        }
        catch (JWTVerificationException exception) {
            return "";
        }
    }

}
