package com.example.foneproject.service.impl;

import com.example.foneproject.entity.Customer;
import com.example.foneproject.exception.InvalidEmailException;
import com.example.foneproject.exception.ResourceNotFoundException;
import com.example.foneproject.repository.CustomerRepository;
import com.example.foneproject.service.CustomerService;
import com.example.foneproject.service.UserCredentialsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final PasswordEncoder passwordEncoder;
    private final CustomerRepository customerRepository;
    private final UserCredentialsService userCredentialsService;

    @Override
    public void save(Customer customer) {
        String email = customer.getEmail();
        if (customerRepository.existsByEmail(email)) {
            throw new InvalidEmailException(email);
        }
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        customerRepository.save(customer);

        userCredentialsService.save(customer);
    }

    @Override
    public Customer get(String email) {
        Optional<Customer> customerOptional = customerRepository.findByEmail(email);
        if (customerOptional.isEmpty()) {
            throw new ResourceNotFoundException(email);
        }
        return customerOptional.get();
    }

    @Override
    public Page<Customer> getAll(int page) {
        List<Customer> customers = customerRepository.findAll();
        Pageable pageable = PageRequest.of(page, 3);
        return customerRepository.findAll(pageable);
    }

    @Override
    @Transactional
    public void update(String email, Customer customer) {
        if (!customerRepository.existsByEmail(email)) {
            throw new ResourceNotFoundException(email);
        }
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        customerRepository.updateByEmail(customer.getFirstName(), customer.getLastName(), customer.getPassword(), email);

        userCredentialsService.update(email, customer);
    }
}
