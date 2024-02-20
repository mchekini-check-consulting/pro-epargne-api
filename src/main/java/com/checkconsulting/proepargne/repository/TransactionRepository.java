package com.checkconsulting.proepargne.repository;

import com.checkconsulting.proepargne.enums.ContributionStatus;
import com.checkconsulting.proepargne.enums.PlanType;
import com.checkconsulting.proepargne.model.Account;
import com.checkconsulting.proepargne.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("SELECT t FROM Transaction t WHERE t.account IN :accounts AND (LOWER(t.type) LIKE LOWER(CONCAT('%', :filter, '%')) or :filter is null or :filter='')  ")
    Page<Transaction> findAllByAccountIn(List<Account> accounts, Pageable pageable, String filter);

    List<Transaction> findByAccountAndCreatedAtAfter(Account account, LocalDateTime startOfYearDate);

    @Query("SELECT t FROM Transaction t WHERE (:planType IS NULL OR t.planType = :planType OR t.planType = '' OR :planType = '' ) AND (t.contribution.status = :status OR :status = '' OR :status IS NULL)")
    Page<Transaction> findAllByPlanType(Pageable pageable, PlanType planType, ContributionStatus status);

}
