package com.checkconsulting.proepargne.repository;

import com.checkconsulting.proepargne.model.Account;
import com.checkconsulting.proepargne.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    Page<Transaction> findAllByAccountIn(List<Account> accounts, Pageable pageable);
}
