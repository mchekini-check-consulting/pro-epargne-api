package com.checkconsulting.proepargne.repository;

import com.checkconsulting.proepargne.model.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Long> {

    public Optional<Contract> findByCompanyAdminId(String adminId);
}
