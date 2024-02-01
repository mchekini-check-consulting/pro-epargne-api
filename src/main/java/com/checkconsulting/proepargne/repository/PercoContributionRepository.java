package com.checkconsulting.proepargne.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.checkconsulting.proepargne.model.PercoContribution;

@Repository
public interface PercoContributionRepository extends JpaRepository<PercoContribution, Long> {

}
