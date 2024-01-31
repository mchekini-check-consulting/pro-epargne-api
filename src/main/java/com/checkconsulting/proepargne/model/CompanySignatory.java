package com.checkconsulting.proepargne.model;


import jakarta.persistence.*;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
