package com.example.foneproject.service.impl;

import com.example.foneproject.dto.request.AuthoritiesReqDTO;
import com.example.foneproject.entity.Customer;
import com.example.foneproject.entity.UserCredentials;
import com.example.foneproject.exception.ResourceNotFoundException;
import com.example.foneproject.handler.ErrorResponseHandler;
import com.example.foneproject.handler.OkResponseHandler;
import com.example.foneproject.repository.UserCredentialsRepository;
import com.example.foneproject.service.UserCredentialsService;
import com.example.foneproject.util.ApiResponse;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserCredentialsServiceImpl implements UserCredentialsService {
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final OkResponseHandler okResponseHandler;
    private final ErrorResponseHandler errorResponseHandler;
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
        try {
            UserCredentials userCredentials = new UserCredentials();
            userCredentials.setEmail(ADMIN_EMAIL);
            userCredentials.setPassword(passwordEncoder.encode(ADMIN_SECRET));
            userCredentials.setEnabled(true);
            userCredentials.setAuthorities(ADMIN_AUTHORITIES);
            userCredentialsRepository.save(userCredentials);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public void save(Customer customer) {
        try {
            UserCredentials userCredentials = modelMapper.map(customer, UserCredentials.class);
            userCredentials.setEnabled(true);
            userCredentials.setAuthorities(CUSTOMER_AUTHORITIES);
            userCredentialsRepository.save(userCredentials);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Override
    @Transactional
    public void updateEmail(String currentEmail, String updatedEmail) {
        try {
            userCredentialsRepository.updateEmail(currentEmail, updatedEmail);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Override
    @Transactional
    public void updatePassword(String email, String updatedPassword) {
        try {
            userCredentialsRepository.updatePassword(email, passwordEncoder.encode(updatedPassword));
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Override
    @Transactional
    public ResponseEntity<ApiResponse> disableUser(String email) {
        try {
            if (!userCredentialsRepository.existsByEmail(email)) {
                throw new ResourceNotFoundException(email);
            }
            userCredentialsRepository.disableByEmail(email);
            return okResponseHandler.get("User: " + email + " disabled", HttpStatus.OK);
        } catch (Exception e) {
            return errorResponseHandler.get(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    @Transactional
    public ResponseEntity<ApiResponse> enableUser(String email) {
        try {
            if (!userCredentialsRepository.existsByEmail(email)) {
                throw new ResourceNotFoundException(email);
            }
            userCredentialsRepository.enableByEmail(email);
            return okResponseHandler.get("User: " + email + " enabled", HttpStatus.OK);
        } catch (Exception e) {
            return errorResponseHandler.get(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    @Transactional
    public ResponseEntity<ApiResponse> assignRole(String email, AuthoritiesReqDTO authoritiesReqDTO) {
        try {
            if (!userCredentialsRepository.existsByEmail(email)) {
                throw new ResourceNotFoundException(email);
            }
            userCredentialsRepository.assignRoleByEmail(email, authoritiesReqDTO.getAuthorities());
            return okResponseHandler.get("Updated roles for user: " + email, HttpStatus.OK);
        } catch (Exception e) {
            return errorResponseHandler.get(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
