package com.checkconsulting.proepargne.repository;

import com.checkconsulting.proepargne.enums.PlanType;
import com.checkconsulting.proepargne.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findAccountByCollaboratorIdAndType(Long collaborator_id, PlanType type);
    List<Account> findAccountByCollaboratorId(Long collaborator_id);

    @Modifying
    @Query("UPDATE Account a " +
            "SET a.amount = (a.amount + :amount) " +
            "WHERE a.accountId = :accountId")
    void updateAccountAmount(Long accountId, Float amount);
}