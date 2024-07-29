package com.example.foneproject.service;

import com.example.foneproject.entity.Customer;

public interface UserCredentialsService {
    void save(Customer customer);

    void updateEmail(String email, Customer customer);

    void updatePassword(String email, Customer customer);

    void loadAdmin();

    void disableUser(String email);

    void enableUser(String email);

    void assignRole(String authorities, String email);
}
