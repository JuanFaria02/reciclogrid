package com.collector.monitoring.reciclogrid.controller;

import com.collector.monitoring.reciclogrid.domain.Employee;
import com.collector.monitoring.reciclogrid.domain.Microcontroller;
import com.collector.monitoring.reciclogrid.domain.dto.*;
import com.collector.monitoring.reciclogrid.infra.security.MicrocontrollerAuthenticationToken;
import com.collector.monitoring.reciclogrid.infra.security.TokenService;
import com.collector.monitoring.reciclogrid.service.EmployeeService;
import com.collector.monitoring.reciclogrid.service.exception.DatabaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

import static com.collector.monitoring.reciclogrid.utils.Constants.*;

@RestController
@RequestMapping
public class AuthenticationController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private TokenService tokenService;

    @PostMapping(LOGIN_PATH)
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Validated AuthenticationDTO data){
        UsernamePasswordAuthenticationToken emailPassword = new UsernamePasswordAuthenticationToken(data.email(), data.password());
        var auth = authenticationManager.authenticate(emailPassword);

        return ResponseEntity.ok(buildLoginResponse((Employee) auth.getPrincipal()));
    }

    @PostMapping(REFRESH_TOKEN_PATH)
    public ResponseEntity<?> authRefreshToken(@RequestBody RefreshTokenDTO refreshTokenDTO, @RequestHeader String authorization) {
        try {
            String email = tokenService.validateToken(refreshTokenDTO.refreshToken());

            Employee employee = employeeService.findByEmail(email);
            return ResponseEntity.ok(buildLoginResponse(employee));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("{\"error\": \"Unauthorized\", \"message\": \"" + e.getMessage() + "\"}");
        }
    }

    @PostMapping("/auth/microcontroller")
    public ResponseEntity<MicrocontrollerTokenDTO> authMicrocontroller(@RequestBody MicrocontrollerDTO microcontrollerDTO) {
        Authentication authentication = new MicrocontrollerAuthenticationToken(microcontrollerDTO.name(), microcontrollerDTO.identifierNumber());
        var authResult = authenticationManager.authenticate(authentication);

        Microcontroller microcontroller = (Microcontroller) authResult.getPrincipal();
        return ResponseEntity.ok(new MicrocontrollerTokenDTO(tokenService.generateMicrocontrollerToken(microcontroller.getName())));
    }

    @PostMapping(API_PATH + "/employee/register")
    public ResponseEntity<EmployeeDTO> register(@RequestBody @Validated RegisterDTO data){
        if (employeeService.findByEmail(data.email()) != null) {
            throw new DatabaseException("email ja cadastrado!");
        }

        String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
        Employee newEmployee = new Employee(null, data.name(), data.email(), data.phone(), data.company(), data.type(), data.documentNumber(), true, encryptedPassword);

        employeeService.insert(newEmployee);

        final URI uri = ServletUriComponentsBuilder.fromUriString("/api/employee/{id}")
                .buildAndExpand(newEmployee.getId()).toUri();

        return ResponseEntity.created(uri).build();
    }

    private LoginResponseDTO buildLoginResponse(Employee user) {
        String accessToken = tokenService.generateAccessToken(user);
        String refreshToken = tokenService.generateRefreshToken(user);

        return new LoginResponseDTO(accessToken,
                refreshToken);
    }
}
