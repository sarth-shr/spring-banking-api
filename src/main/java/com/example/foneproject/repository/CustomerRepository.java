package com.example.foneproject.repository;

import com.example.foneproject.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, String> {
    boolean existsByEmail(String email);

    Optional<Customer> findByEmail(String email);

    @Modifying
    @Query("update Customer c set c.firstName= ?1, c.lastName= ?2, c.password= ?3 where c.email= ?4")
    void updateByEmail(String firstName, String lastName, String password, String email);
}
