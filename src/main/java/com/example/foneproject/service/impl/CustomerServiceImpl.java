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
import com.example.foneproject.repository.CustomerRepository;
import com.example.foneproject.service.CustomerService;
import com.example.foneproject.service.UserCredentialsService;
import com.example.foneproject.util.ApiResponse;
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
    public ResponseEntity<ApiResponse> save(CustomerReqDTO customerReqDTO) {
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
    public ResponseEntity<ApiResponse> get(String email) {
        try {
            Optional<Customer> customerOptional = customerRepository.findByEmail(email);
            if (customerOptional.isEmpty()) {
                throw new ResourceNotFoundException(email);
            }
            Customer customer = customerOptional.get();
            CustomerResDTO customerResDTO = modelMapper.map(customer, CustomerResDTO.class);

            return okResponseHandler.getContent("Retrieved customer with email: " + email, HttpStatus.OK, customerResDTO);
        } catch (Exception e) {
            return errorResponseHandler.get(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<ApiResponse> getAll(int page) {
        try {
            Pageable pageable = PageRequest.of(page, 3);

            Page<Customer> customerPage = customerRepository.findAll(pageable);
            Page<CustomerResDTO> customerDTOPage = customerPage.map(customer -> modelMapper.map(customer, CustomerResDTO.class));

            return okResponseHandler.getPaginated("Retrieved customers list", HttpStatus.OK, customerDTOPage);
        } catch (Exception e) {
            return errorResponseHandler.get(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    @Transactional
    public ResponseEntity<ApiResponse> updatePersonal(String email, CustomerInfoReqDTO customerInfoReqDTO) {
        try {
            if (!customerRepository.existsByEmail(email)) {
                throw new ResourceNotFoundException(email);
            }
            String firstName = customerInfoReqDTO.getFirstName();
            String lastName = customerInfoReqDTO.getLastName();
            customerRepository.updatePersonalDetails(firstName, lastName, email);

            return okResponseHandler.get("Updated personal details of email: " + email, HttpStatus.OK);
        } catch (Exception e) {
            return errorResponseHandler.get(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    @Transactional
    public ResponseEntity<ApiResponse> updateEmail(String email, CustomerEmailReqDTO customerEmailReqDTO) {
        try {
            Customer customer = modelMapper.map(customerEmailReqDTO, Customer.class);
            if (!customerRepository.existsByEmail(email)) {
                throw new ResourceNotFoundException(email);
            }
            if (customerRepository.existsByEmail(customer.getEmail())) {
                throw new InvalidEmailException(customer.getEmail());
            }
            customerRepository.updateEmail(email, customer.getEmail());

            userCredentialsService.updateEmail(email, customer.getEmail());
            return okResponseHandler.get("Email changed successfully", HttpStatus.OK);
        } catch (Exception e) {
            return errorResponseHandler.get(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    @Transactional
    public ResponseEntity<ApiResponse> updatePassword(String email, CustomerPasswordReqDTO customerPasswordReqDTO) {
        try {
            Optional<Customer> customerOptional = customerRepository.findByEmail(email);
            if (customerOptional.isEmpty()) {
                throw new ResourceNotFoundException(email);
            }

            String currentPassword = customerPasswordReqDTO.getCurrentPassword();
            String hashedPassword = customerOptional.get().getPassword();

            if (!passwordEncoder.matches(currentPassword, hashedPassword)) {
                throw new InvalidCredentialsException();
            }

            String updatedPassword = customerPasswordReqDTO.getUpdatedPassword();
            customerRepository.updatePassword(email, passwordEncoder.encode(updatedPassword));
            userCredentialsService.updatePassword(email, updatedPassword);

            return okResponseHandler.get("Password changed successfully", HttpStatus.OK);
        } catch (Exception e) {
            return errorResponseHandler.get(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
