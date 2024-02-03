package com.checkconsulting.proepargne.model;


import com.checkconsulting.proepargne.enums.Eligibility;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.*;
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
    @OneToOne(mappedBy = "contract", cascade = PERSIST)
    private Company company;

    @OneToOne(mappedBy = "contract", cascade = PERSIST)
    private CompanySignatory companySignatory;

    @OneToOne(mappedBy = "contract", cascade = PERSIST)
    private PeeContribution peeContribution;

    @OneToOne(mappedBy = "contract", cascade = PERSIST)
    private PerecoContribution perecoContribution;

    @OneToMany(mappedBy = "contract", cascade = PERSIST)
    private List<Account> accountList = new ArrayList<>();

}
