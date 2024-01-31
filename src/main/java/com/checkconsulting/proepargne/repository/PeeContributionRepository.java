package com.checkconsulting.proepargne.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.checkconsulting.proepargne.model.Contract;
import com.checkconsulting.proepargne.model.PeeContribution;

@Repository
public interface PeeContributionRepository extends JpaRepository<PeeContribution, Contract> {

}
