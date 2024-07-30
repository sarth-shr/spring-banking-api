package com.example.foneproject.repository;

import com.example.foneproject.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    @Query("select t from Transaction t where t.fromAccount.id=?1 or t.toAccount.id=?1")
    List<Transaction> findByAccountId(int id);

    @Query("select t from Transaction t where t.fromAccount.id=?1 or t.toAccount.id=?1")
    Page<Transaction> findByAccountPageable(Pageable pageable, int accId);
}
