package com.example.foneproject.service.impl;

import com.example.foneproject.entity.Customer;
import com.example.foneproject.entity.UserCredentials;
import com.example.foneproject.exception.ResourceNotFoundException;
import com.example.foneproject.repository.UserCredentialsRepository;
import com.example.foneproject.service.UserCredentialsService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserCredentialsServiceImpl implements UserCredentialsService {
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserCredentialsRepository userCredentialsRepository;

    @Value("${credentials.admin.secret}")
    private String ADMIN_SECRET;

    @Override
    @PostConstruct
    public void loadAdmin() {
        UserCredentials userCredentials = new UserCredentials();
        userCredentials.setEmail("admin@admin.com");
        userCredentials.setPassword(passwordEncoder.encode(ADMIN_SECRET));
        userCredentials.setEnabled(true);
        userCredentials.setAuthorities("ADMIN");
        userCredentialsRepository.save(userCredentials);
    }

    @Override
    public void save(Customer customer) {
        UserCredentials userCredentials = modelMapper.map(customer, UserCredentials.class);
        userCredentials.setEnabled(true);
        userCredentials.setAuthorities("CUSTOMER");
        userCredentialsRepository.save(userCredentials);
    }

    @Override
    @Transactional
    public void update(String email, Customer customer) {
        UserCredentials userCredentials = modelMapper.map(customer, UserCredentials.class);
        userCredentialsRepository.updateByEmail(email, userCredentials.getPassword(), "CUSTOMER");
    }

    @Override
    @Transactional
    public void disableUser(String email) {
        if (!userCredentialsRepository.existsByEmail(email)) {
            throw new ResourceNotFoundException(email);
        }
        userCredentialsRepository.disableByEmail(email);
    }

    @Override
    @Transactional
    public void enableUser(String email) {
        if (!userCredentialsRepository.existsByEmail(email)) {
            throw new ResourceNotFoundException(email);
        }
        userCredentialsRepository.enableByEmail(email);
    }

    @Override
    @Transactional
    public void assignRole(String authorities, String email) {
        if (!userCredentialsRepository.existsByEmail(email)) {
            throw new ResourceNotFoundException(email);
        }
        userCredentialsRepository.assignRoleByEmail(authorities, email);
    }
}
