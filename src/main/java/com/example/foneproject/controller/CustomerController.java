package com.example.foneproject.controller;

import com.example.foneproject.dto.request.CustomerRequestDTO;
import com.example.foneproject.dto.request.CustomerUpdateRequestDTO;
import com.example.foneproject.dto.response.CustomerResponseDTO;
import com.example.foneproject.entity.Customer;
import com.example.foneproject.handler.JsonResponseHandler;
import com.example.foneproject.handler.PaginationResponseHandler;
import com.example.foneproject.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/customers")
public class CustomerController {
    private final ModelMapper modelMapper;
    private final CustomerService customerService;
    private final JsonResponseHandler jsonResponseHandler;

    @PostMapping
    public ResponseEntity<Map<String, Object>> saveUser(@Valid @RequestBody CustomerRequestDTO customerRequestDTO) {
        Customer customer = modelMapper.map(customerRequestDTO, Customer.class);
        customerService.save(customer);

        return jsonResponseHandler.get("Email registered successfully", HttpStatus.CREATED.value(), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getUsers(@RequestParam(name = "page", defaultValue = "0") int page) {
        Page<Customer> customers = customerService.getAll(page);
        Page<CustomerResponseDTO> customerResponseDTOPage = customers.map(customer -> modelMapper.map(customer, CustomerResponseDTO.class));
        PaginationResponseHandler<CustomerResponseDTO> customerPage = new PaginationResponseHandler<>(customerResponseDTOPage);

        return jsonResponseHandler.get("Retrieved customers list", HttpStatus.OK.value(), HttpStatus.OK, customerPage);
    }

    @GetMapping("/get")
    public ResponseEntity<Map<String, Object>> getUser(@RequestParam("email") String email) {
        Customer customer = customerService.get(email);
        CustomerResponseDTO customerResponseDTO = modelMapper.map(customer, CustomerResponseDTO.class);

        return jsonResponseHandler.get("Retrieved customer with email: " + email, HttpStatus.OK.value(), HttpStatus.OK, customerResponseDTO);
    }

    @PutMapping("/update")
    public ResponseEntity<Map<String, Object>> updateUser(@RequestParam("email") String email, @Valid @RequestBody CustomerUpdateRequestDTO customerUpdateRequestDTO) {
        Customer customer = modelMapper.map(customerUpdateRequestDTO, Customer.class);
        customerService.update(email, customer);

        return jsonResponseHandler.get("Updated user with email: " + email, HttpStatus.OK.value(), HttpStatus.OK);
    }
}
