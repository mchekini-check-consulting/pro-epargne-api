package com.checkconsulting.proepargne.repository;

import com.checkconsulting.proepargne.model.Contribution;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContributionRepository extends JpaRepository<Contribution,Long> {
}
