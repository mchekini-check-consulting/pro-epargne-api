package com.checkconsulting.proepargne.model;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class CompanySignatory {

    @Id
    private Long id;
    private String lastName;
    private String firstName;
    private String email;
    private LocalDate dateOfBirth;
    private String jobTitle;
    private String socialSecurityNumber;
    private String countryOfBirth;
    private String countryOfResidence;
    private Boolean executive;

    @OneToOne
    @JoinColumn(name = "contract_id")
    private Contract contractId;
}
