package com.checkconsulting.proepargne.repository;

import com.checkconsulting.proepargne.model.Account;
import com.checkconsulting.proepargne.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("SELECT t FROM Transaction t WHERE t.account IN :accounts AND (LOWER(t.type) LIKE LOWER(CONCAT('%', :filter, '%')) or :filter is null or :filter='')  ")
    Page<Transaction> findAllByAccountIn(List<Account> accounts, Pageable pageable, String filter);

}
