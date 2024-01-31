package com.checkconsulting.proepargne.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.checkconsulting.proepargne.model.Contract;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Long> {

}
