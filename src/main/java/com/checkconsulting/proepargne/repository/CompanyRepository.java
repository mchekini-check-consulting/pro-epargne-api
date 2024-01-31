package com.checkconsulting.proepargne.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.checkconsulting.proepargne.model.Company;

@Repository
public interface CompanyRepository extends JpaRepository<Company, String> {

}
