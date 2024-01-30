package com.checkconsulting.proepargne.model;


import com.checkconsulting.proepargne.enums.Eligibility;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import static jakarta.persistence.EnumType.STRING;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Contract {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long contractId;
    private Integer closingMonth;
    @Enumerated(STRING)
    private Eligibility eligibility;
    @OneToOne(mappedBy = "contractId")
    private Company company;

    @OneToOne(mappedBy = "contractId")
    private CompanySignatory companySignatory;

    @OneToOne(mappedBy = "contractId")
    private PeeContribution peeContribution;

    @OneToOne(mappedBy = "contractId")
    private PerecoContribution perecoContribution;

    @OneToMany(mappedBy = "contract")
    private List<Account> accountList;

}
