package com.example.foneproject.repository;

import com.example.foneproject.entity.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Integer> {
    List<Account> findByCustomer_Email(String email);

    Optional<Account> findByAccNumber(String accNumber);

    @Modifying
    @Query("update Account a set a.balance=(a.balance+?2) where a.accNumber= ?1")
    void increaseFunds(String accNumber, int amount);

    @Modifying
    @Query("update Account a set a.balance=(a.balance-?2) where a.accNumber= ?1")
    void decreaseFunds(String accNumber, int amount);

    @Query("select a from Account a where a.customer.email= ?1")
    Page<Account> findByEmailPageable(String email, Pageable pageable);

}
