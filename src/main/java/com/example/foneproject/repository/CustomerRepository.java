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
    @Query("update Customer c set c.firstName= ?1, c.lastName= ?2 where c.email= ?3")
    void updatePersonalDetails(String firstName, String lastName, String email);

    @Modifying
    @Query("update Customer c set c.email= ?2 where c.email = ?1")
    void updateEmail(String currentEmail, String newEmail);

    @Modifying
    @Query("update Customer c set c.password= ?1 where c.email= ?2")
    void updatePassword(String password, String email);
}
