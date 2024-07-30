package com.example.foneproject.service.impl;

import com.example.foneproject.dto.request.AuthoritiesReqDTO;
import com.example.foneproject.entity.Customer;
import com.example.foneproject.entity.UserCredentials;
import com.example.foneproject.exception.ResourceNotFoundException;
import com.example.foneproject.handler.JsonResponseHandler;
import com.example.foneproject.repository.UserCredentialsRepository;
import com.example.foneproject.service.UserCredentialsService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserCredentialsServiceImpl implements UserCredentialsService {
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final JsonResponseHandler jsonResponseHandler;
    private final UserCredentialsRepository userCredentialsRepository;

    @Value("${credentials.admin.secret}")
    private String ADMIN_SECRET;

    @Value("${credentials.admin.email}")
    private String ADMIN_EMAIL;

    @Value("${credentials.admin.authorities}")
    private String ADMIN_AUTHORITIES;

    @Value("${credentials.customer.authorities}")
    private String CUSTOMER_AUTHORITIES;

    @Override
    @PostConstruct
    public void loadAdmin() {
        UserCredentials userCredentials = new UserCredentials();
        userCredentials.setEmail(ADMIN_EMAIL);
        userCredentials.setPassword(passwordEncoder.encode(ADMIN_SECRET));
        userCredentials.setEnabled(true);
        userCredentials.setAuthorities(ADMIN_AUTHORITIES);
        userCredentialsRepository.save(userCredentials);
    }

    @Override
    public void save(Customer customer) {
        UserCredentials userCredentials = modelMapper.map(customer, UserCredentials.class);
        userCredentials.setEnabled(true);
        userCredentials.setAuthorities(CUSTOMER_AUTHORITIES);
        userCredentialsRepository.save(userCredentials);
    }

    @Override
    @Transactional
    public void updateEmail(String email, Customer customer) {
        UserCredentials userCredentials = modelMapper.map(customer, UserCredentials.class);
        userCredentialsRepository.updateEmail(email, userCredentials.getEmail());
    }

    @Override
    @Transactional
    public void updatePassword(String email, Customer customer) {
        UserCredentials userCredentials = modelMapper.map(customer, UserCredentials.class);
        userCredentialsRepository.updatePassword(email, passwordEncoder.encode(userCredentials.getPassword()));
    }

    @Override
    @Transactional
    public ResponseEntity<Map<String, Object>> disableUser(String email) {
        if (!userCredentialsRepository.existsByEmail(email)) {
            throw new ResourceNotFoundException(email);
        }
        userCredentialsRepository.disableByEmail(email);
        return jsonResponseHandler.get("User: " + email + " disabled", HttpStatus.OK.value(), HttpStatus.OK);
    }

    @Override
    @Transactional
    public ResponseEntity<Map<String, Object>> enableUser(String email) {
        if (!userCredentialsRepository.existsByEmail(email)) {
            throw new ResourceNotFoundException(email);
        }
        userCredentialsRepository.enableByEmail(email);
        return jsonResponseHandler.get("User: " + email + " enabled", HttpStatus.OK.value(), HttpStatus.OK);
    }

    @Override
    @Transactional
    public ResponseEntity<Map<String, Object>> assignRole(String email, AuthoritiesReqDTO authoritiesReqDTO) {
        if (!userCredentialsRepository.existsByEmail(email)) {
            throw new ResourceNotFoundException(email);
        }
        userCredentialsRepository.assignRoleByEmail(email, authoritiesReqDTO.getAuthorities());
        return jsonResponseHandler.get("Updated roles for user: " + email, HttpStatus.OK.value(), HttpStatus.OK);
    }
}
