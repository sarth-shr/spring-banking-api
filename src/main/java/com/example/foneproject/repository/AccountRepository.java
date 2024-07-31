package com.example.foneproject.repository;

import com.example.foneproject.entity.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Integer> {
    List<Account> findByCustomer_Email(String email);

    @Modifying
    @Query("update Account a set a.balance=(a.balance+?2) where a.id= ?1")
    void depositAmount(int id, int amount);

    @Query("select a from Account a where a.customer.email= ?1")
    Page<Account> findByEmailPageable(Pageable pageable, String email);

}
