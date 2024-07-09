package com.example.foneproject.repository;

import com.example.foneproject.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Integer> {
    List<Account> findByCustomer_Email(String email);

    boolean existsByType(String type);
}
