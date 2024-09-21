package com.example.foneproject.repository;

import com.example.foneproject.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    List<Transaction> findByFromAccount_AccNumberOrToAccount_AccNumber(String fromAccNumber, String toAccNumber);

    Page<Transaction> findByFromAccount_AccNumberOrToAccount_AccNumber(String fromAccNumber, String toAccNumber, Pageable pageable);

    @Query("select t from Transaction t where t.fromAccount.accNumber=?1 or t.toAccount.accNumber=?1")
    Page<Transaction> findByAccNumber(String accNumber, Pageable pageable);

    Optional<Transaction> findByTransactionNumber(String transactionNumber);
}
