package com.example.foneproject.controller;

import com.example.foneproject.dto.request.CustomerEmailReqDTO;
import com.example.foneproject.dto.request.CustomerInfoReqDTO;
import com.example.foneproject.dto.request.CustomerPasswordReqDTO;
import com.example.foneproject.dto.request.CustomerReqDTO;
import com.example.foneproject.service.CustomerService;
import com.example.foneproject.util.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/customers")
public class CustomerController {
    private final CustomerService customerService;

    @GetMapping("/get")
    public ResponseEntity<ApiResponse> getUser(@RequestParam("email") String email) {
        return customerService.get(email);
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getUsers(@RequestParam(name = "page", defaultValue = "0") int page) {
        return customerService.getAll(page);
    }

    @PostMapping
    public ResponseEntity<ApiResponse> saveUser(@Valid @RequestBody CustomerReqDTO customerReqDTO) {
        return customerService.save(customerReqDTO);
    }

    @PutMapping("/update/personal")
    public ResponseEntity<ApiResponse> updateUserInformation(@RequestParam("email") String email, @Valid @RequestBody CustomerInfoReqDTO customerInfoReqDTO) {
        return customerService.updatePersonal(email, customerInfoReqDTO);
    }

    @PutMapping("/update/email")
    public ResponseEntity<ApiResponse> updateUserEmail(@RequestParam("email") String email, @Valid @RequestBody CustomerEmailReqDTO customerEmailReqDTO) {
        return customerService.updateEmail(email, customerEmailReqDTO);
    }

    @PutMapping("update/password")
    public ResponseEntity<ApiResponse> updateUserPassword(@RequestParam("email") String email, @Valid @RequestBody CustomerPasswordReqDTO customerPasswordReqDTO) {
        return customerService.updatePassword(email, customerPasswordReqDTO);
    }
}
