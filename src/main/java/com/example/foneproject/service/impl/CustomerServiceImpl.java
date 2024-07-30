package com.example.foneproject.service.impl;

import com.example.foneproject.dto.request.CustomerEmailReqDTO;
import com.example.foneproject.dto.request.CustomerInfoReqDTO;
import com.example.foneproject.dto.request.CustomerPasswordReqDTO;
import com.example.foneproject.dto.request.CustomerReqDTO;
import com.example.foneproject.dto.response.CustomerResDTO;
import com.example.foneproject.entity.Customer;
import com.example.foneproject.exception.InvalidCredentialsException;
import com.example.foneproject.exception.InvalidEmailException;
import com.example.foneproject.exception.ResourceNotFoundException;
import com.example.foneproject.handler.ErrorResponseHandler;
import com.example.foneproject.handler.OkResponseHandler;
import com.example.foneproject.handler.PaginationResponseHandler;
import com.example.foneproject.repository.CustomerRepository;
import com.example.foneproject.service.CustomerService;
import com.example.foneproject.service.UserCredentialsService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final OkResponseHandler okResponseHandler;
    private final CustomerRepository customerRepository;
    private final ErrorResponseHandler errorResponseHandler;
    private final UserCredentialsService userCredentialsService;

    @Override
    public ResponseEntity<Map<String, Object>> save(CustomerReqDTO customerReqDTO) {
        try {
            Customer customer = modelMapper.map(customerReqDTO, Customer.class);
            String email = customer.getEmail();
            if (customerRepository.existsByEmail(email)) {
                throw new InvalidEmailException(email);
            }
            customer.setPassword(passwordEncoder.encode(customer.getPassword()));
            customerRepository.save(customer);

            userCredentialsService.save(customer);

            return okResponseHandler.get("Email registered successfully", HttpStatus.CREATED);
        } catch (Exception e) {
            return errorResponseHandler.get(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<Map<String, Object>> get(String email) {
        try {
            Optional<Customer> customerOptional = customerRepository.findByEmail(email);
            if (customerOptional.isEmpty()) {
                throw new ResourceNotFoundException(email);
            }
            Customer customer = customerOptional.get();
            CustomerResDTO customerResDTO = modelMapper.map(customer, CustomerResDTO.class);

            return okResponseHandler.get("Retrieved customer with email: " + email, HttpStatus.OK, customerResDTO);
        } catch (Exception e) {
            return errorResponseHandler.get(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<Map<String, Object>> getAll(int page) {
        try {
            Pageable pageable = PageRequest.of(page, 3);

            Page<Customer> customerPage = customerRepository.findAll(pageable);
            Page<CustomerResDTO> customerDTOPage = customerPage.map(customer -> modelMapper.map(customer, CustomerResDTO.class));
            PaginationResponseHandler<CustomerResDTO> customers = new PaginationResponseHandler<>(customerDTOPage);

            return okResponseHandler.get("Retrieved customers list", HttpStatus.OK, customers);
        } catch (Exception e) {
            return errorResponseHandler.get(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    @Transactional
    public ResponseEntity<Map<String, Object>> updatePersonal(String email, CustomerInfoReqDTO customerInfoReqDTO) {
        try {
            if (!customerRepository.existsByEmail(email)) {
                throw new ResourceNotFoundException(email);
            }
            Customer customer = modelMapper.map(customerInfoReqDTO, Customer.class);
            customerRepository.updatePersonalDetails(customer.getFirstName(), customer.getLastName(), email);

            return okResponseHandler.get("Updated personal details of email: " + email, HttpStatus.OK);
        } catch (Exception e) {
            return errorResponseHandler.get(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    @Transactional
    public ResponseEntity<Map<String, Object>> updateEmail(String email, CustomerEmailReqDTO customerEmailReqDTO) {
        try {
            Customer customer = modelMapper.map(customerEmailReqDTO, Customer.class);
            if (!customerRepository.existsByEmail(email)) {
                throw new ResourceNotFoundException(email);
            }
            if (customerRepository.existsByEmail(customer.getEmail())) {
                throw new InvalidEmailException(customer.getEmail());
            }
            customerRepository.updateEmail(email, customer.getEmail());

            userCredentialsService.updateEmail(email, customer);
            return okResponseHandler.get("Email changed successfully", HttpStatus.OK);
        } catch (Exception e) {
            return errorResponseHandler.get(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    @Transactional
    public ResponseEntity<Map<String, Object>> updatePassword(String email, CustomerPasswordReqDTO customerPasswordReqDTO) {
        try {
            Optional<Customer> customerOptional = customerRepository.findByEmail(email);
            if (customerOptional.isEmpty()) {
                throw new ResourceNotFoundException(email);
            }

            Customer customer = modelMapper.map(customerPasswordReqDTO, Customer.class);

            String currentPassword = customerPasswordReqDTO.getCurrentPassword();
            String hashedPassword = customerOptional.get().getPassword();
            if (!passwordEncoder.matches(currentPassword, hashedPassword)) {
                throw new InvalidCredentialsException();
            }

            customerRepository.updatePassword(passwordEncoder.encode(customer.getPassword()), email);
            userCredentialsService.updatePassword(email, customer);

            return okResponseHandler.get("Password changed successfully", HttpStatus.OK);
        } catch (Exception e) {
            return errorResponseHandler.get(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
