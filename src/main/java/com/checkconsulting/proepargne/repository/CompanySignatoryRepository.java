package com.checkconsulting.proepargne.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.checkconsulting.proepargne.model.CompanySignatory;

@Repository
public interface CompanySignatoryRepository extends JpaRepository<CompanySignatory, Long> {

}
