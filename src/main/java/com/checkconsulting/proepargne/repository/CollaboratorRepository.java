package com.checkconsulting.proepargne.repository;

import com.checkconsulting.proepargne.model.Collaborator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CollaboratorRepository extends JpaRepository<Collaborator, Long> {

    Optional<Collaborator> findByEmail(String email);
    @Query(value = "select c from Collaborator c where c in ( select a.collaborator from Account a where a.contract = ( select c from Contract c where c.companyAdminId = ?1))")
    List<Collaborator> findAllCompanyAdminCollaborators(String adminId);
}
