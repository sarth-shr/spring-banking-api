package com.example.foneproject.controller;

import com.example.foneproject.dto.request.CustomerInfoReqDTO;
import com.example.foneproject.dto.request.CustomerReqDTO;
import com.example.foneproject.dto.response.CustomerResDTO;
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
    public ResponseEntity<Map<String, Object>> saveUser(@Valid @RequestBody CustomerReqDTO customerReqDTO) {
        Customer customer = modelMapper.map(customerReqDTO, Customer.class);
        customerService.save(customer);

        return jsonResponseHandler.get("Email registered successfully", HttpStatus.CREATED.value(), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getUsers(@RequestParam(name = "page", defaultValue = "0") int page) {
        Page<Customer> customers = customerService.getAll(page);
        Page<CustomerResDTO> customerResDTOPage = customers.map(customer -> modelMapper.map(customer, CustomerResDTO.class));
        PaginationResponseHandler<CustomerResDTO> customerPage = new PaginationResponseHandler<>(customerResDTOPage);

        return jsonResponseHandler.get("Retrieved customers list", HttpStatus.OK.value(), HttpStatus.OK, customerPage);
    }

    @GetMapping("/get")
    public ResponseEntity<Map<String, Object>> getUser(@RequestParam("email") String email) {
        Customer customer = customerService.get(email);
        CustomerResDTO customerResDTO = modelMapper.map(customer, CustomerResDTO.class);

        return jsonResponseHandler.get("Retrieved customer with email: " + email, HttpStatus.OK.value(), HttpStatus.OK, customerResDTO);
    }

    @PutMapping("/update")
    public ResponseEntity<Map<String, Object>> updateUserInformation(@RequestParam("email") String email, @Valid @RequestBody CustomerInfoReqDTO customerInfoReqDTO) {
        Customer customer = modelMapper.map(customerInfoReqDTO, Customer.class);
        customerService.updatePersonal(email, customer);

        return jsonResponseHandler.get("Updated user with email: " + email, HttpStatus.OK.value(), HttpStatus.OK);
    }
}
