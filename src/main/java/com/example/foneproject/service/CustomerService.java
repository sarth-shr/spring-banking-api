package com.example.foneproject.service;

import com.example.foneproject.entity.Customer;
import org.springframework.data.domain.Page;

public interface CustomerService {
    void save(Customer customer);

    Customer get(String email);

    Page<Customer> getAll(int page);

    void update(String email, Customer customer);
}
